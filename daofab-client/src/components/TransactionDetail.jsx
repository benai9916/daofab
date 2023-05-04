import { Box, Paper, Table, TableBody, TableContainer, TableHead, TableRow } from "@mui/material";
import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { StyledTableCell, StyledTableRow } from "./Common";

export const TransactionDetail = () => {
  let { id } = useParams();
  const [data, setData] = useState({
    rows: [],
  });

  const updateData = (k, v) => setData((prev) => ({ ...prev, [k]: v }));

  const getTransactionDetailData = () => {
    axios
      .get(`http://localhost:8080/api/v1/transaction/${id}`)
      .then((res) => {
        const data = res.data.data;
        updateData("rows", data.transactionDetail);
      });
  };

  useEffect(() => {
    getTransactionDetailData();
  }, []);

  return (
    <Box
    sx={{
      maxWidth: 1200,
      mt: 6,
      mx: "auto",
    }}
  >
    <TableContainer component={Paper}>
      <Table sx={{ minWidth: 700 }} aria-label="customized table">
        <TableHead>
          <TableRow>
            <StyledTableCell>ID</StyledTableCell>
            <StyledTableCell align="right">Sender</StyledTableCell>
            <StyledTableCell align="right">Receiver</StyledTableCell>
            <StyledTableCell align="right">Total Amount</StyledTableCell>
            <StyledTableCell align="right">Paid Amount</StyledTableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {data.rows.map((row) => (
            <StyledTableRow key={row.id}>
              <StyledTableCell component="th" scope="row">
                {row.id}
              </StyledTableCell>
              <StyledTableCell align="right">{row.sender}</StyledTableCell>
              <StyledTableCell align="right">{row.receiver}</StyledTableCell>
              <StyledTableCell align="right">
                {row.totalAmount}
              </StyledTableCell>
              <StyledTableCell align="right">
                {row.paidAmount}
              </StyledTableCell>
            </StyledTableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  </Box>
  )
}

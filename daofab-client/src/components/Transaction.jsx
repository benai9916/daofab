import { Box, Pagination, Paper, Table, TableBody, TableContainer, TableHead, TableRow } from "@mui/material";
import { useEffect, useState } from "react";

import axios from "axios";
import { useNavigate } from "react-router-dom";
import { StyledTableCell, StyledTableRow } from "./Common";

const Transaction = () => {
  const navigate = useNavigate()
  const [data, setData] = useState({
    rows: [],
    totalRows: 2,
  });

  const updateData = (k, v) => setData((prev) => ({ ...prev, [k]: v }));

  const [page, setPage] = useState(1);
  const handleChange = (event, value) => {
    setPage(value);
  };

  const getTransactionData = () => {
    axios
      .get(`http://localhost:8080/api/v1/transaction?pageNo=${page}`)
      .then((res) => {
        const data = res.data.data;
        updateData("rows", data.transaction);
        updateData("totalRows", data.pageContext);
      });
  };

  useEffect(() => {
    getTransactionData();
  }, [page]);


  const handleTnxDetail = (id) => {
    navigate(`/transaction/${id}`)
  }

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
              <StyledTableCell align="right">Total Paid Amount</StyledTableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {data.rows.map((row) => (
              <StyledTableRow key={row.id} onClick={() => handleTnxDetail(row.id)} sx={{ cursor: 'pointer'}}>
                <StyledTableCell component="th" scope="row">
                  {row.id}
                </StyledTableCell>
                <StyledTableCell align="right">{row.sender}</StyledTableCell>
                <StyledTableCell align="right">{row.receiver}</StyledTableCell>
                <StyledTableCell align="right">
                  {row.totalAmount}
                </StyledTableCell>
                <StyledTableCell align="right">
                  {row.totalPaidAmount}
                </StyledTableCell>
              </StyledTableRow>
            ))}
          </TableBody>
        </Table>
        <Pagination
          sx={{
            p: 2,
          }}
          count={data.totalRows}
          boundaryCount={5}
          page={page}
          onChange={handleChange}
          color="primary"
        />
      </TableContainer>
    </Box>
  );
};

export default Transaction;

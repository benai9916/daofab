import { Routes, Route, BrowserRouter } from "react-router-dom";
import Transaction from "./components/Transaction";
import { TransactionDetail } from "./components/TransactionDetail";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/transaction">
          <Route index element={<Transaction />} />
          <Route path=":id" element={<TransactionDetail />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;

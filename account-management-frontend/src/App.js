import React, { useEffect, useState, useContext } from "react";
import { Container, Row, Col, Form, Button, Table, Spinner } from "react-bootstrap";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "./App.css";
import { AuthContext, AuthProvider } from "./AuthContext"; // make sure AuthContext.js exists
import { api, setAuthToken } from "./api";

export default function App() {
  return (
    <AuthProvider>
      <BankingApp />
    </AuthProvider>
  );
}

function BankingApp() {
  const { jwt, setJwt, logout } = useContext(AuthContext);

  const [accounts, setAccounts] = useState([]);
  const [newAccount, setNewAccount] = useState({ type: "SAVINGS", ownerName: "" });
  const [depositData, setDepositData] = useState({ id: "", amount: "" });
  const [withdrawData, setWithdrawData] = useState({ id: "", amount: "" });
  const [transferData, setTransferData] = useState({ from: "", to: "", amount: "" });
  const [loading, setLoading] = useState(false);
  const [loginData, setLoginData] = useState({ username: "", password: "" });
  const [signupData, setSignupData] = useState({ username: "", password: "" });

  const showSuccess = (msg) => toast.success(msg, { position: "bottom-right" });
  const showError = (msg) => toast.error(msg, { position: "bottom-right" });

  // Set JWT for Axios whenever it changes
  useEffect(() => {
    setAuthToken(jwt);
    if (jwt) loadAccounts();
  }, [jwt]);

  const loadAccounts = async () => {
    setLoading(true);
    try {
      const res = await api.get("/accounts");
      setAccounts(res.data);
    } catch {
      showError("❌ Failed to load accounts.");
    } finally {
      setLoading(false);
    }
  };

  const handleSignup = async () => {
    setLoading(true);
    try {
      await api.post("/auth/signup", signupData);
      showSuccess("✅ Signup successful! Logging in...");
      handleLogin(signupData);
    } catch {
      showError("⚠️ Signup failed.");
    } finally {
      setLoading(false);
    }
  };

  const handleLogin = async (data = loginData) => {
    setLoading(true);
    try {
      const res = await api.post("/auth/login", data);
      setJwt(res.data.jwt);
      showSuccess("🔑 Logged in successfully!");
    } catch {
      showError("❌ Login failed.");
    } finally {
      setLoading(false);
    }
  };

  const createAccount = async () => {
    setLoading(true);
    try {
      await api.post("/accounts", newAccount);
      setNewAccount({ type: "SAVINGS", ownerName: "" });
      loadAccounts();
      showSuccess("✅ Account created!");
    } catch {
      showError("⚠️ Error creating account.");
    } finally {
      setLoading(false);
    }
  };

  const deposit = async () => {
    setLoading(true);
    try {
      await api.post(`/accounts/${depositData.id}/deposit`, {
        accountId: Number(depositData.id),
        amountInPaise: Number(depositData.amount) * 100,
      });
      setDepositData({ id: "", amount: "" });
      loadAccounts();
      showSuccess("💰 Deposit successful!");
    } catch {
      showError("❌ Deposit failed.");
    } finally {
      setLoading(false);
    }
  };

  const withdraw = async () => {
    setLoading(true);
    try {
      await api.post(`/accounts/${withdrawData.id}/withdraw`, {
        accountId: Number(withdrawData.id),
        amountInPaise: Number(withdrawData.amount) * 100,
      });
      setWithdrawData({ id: "", amount: "" });
      loadAccounts();
      showSuccess("💳 Withdrawal successful!");
    } catch {
      showError("❌ Withdrawal failed.");
    } finally {
      setLoading(false);
    }
  };

  const transfer = async () => {
    setLoading(true);
    try {
      await api.post("/accounts/transfer", {
        fromAccountId: Number(transferData.from),
        toAccountId: Number(transferData.to),
        amountInPaise: Number(transferData.amount) * 100,
      });
      setTransferData({ from: "", to: "", amount: "" });
      loadAccounts();
      showSuccess("🔄 Transfer successful!");
    } catch {
      showError("❌ Transfer failed.");
    } finally {
      setLoading(false);
    }
  };

  const applyInterest = async (id) => {
    setLoading(true);
    try {
      await api.post(`/accounts/${id}/apply-interest`);
      loadAccounts();
      showSuccess("✨ Interest applied!");
    } catch {
      showError("❌ Failed to apply interest.");
    } finally {
      setLoading(false);
    }
  };

  // ===== UI =====
  if (!jwt) {
    return (
      <Container className="mt-4">
        <h2 className="text-center mb-4">🏦 Banking Dashboard - Login / Signup</h2>
        <Row>
          <Col md={6}>
            <h5>🔑 Login</h5>
            <Form>
              <Form.Control
                placeholder="Username"
                value={loginData.username}
                onChange={(e) => setLoginData({ ...loginData, username: e.target.value })}
                className="mb-2"
              />
              <Form.Control
                type="password"
                placeholder="Password"
                value={loginData.password}
                onChange={(e) => setLoginData({ ...loginData, password: e.target.value })}
                className="mb-2"
              />
              <Button onClick={() => handleLogin()} disabled={loading}>
                Login
              </Button>
            </Form>
          </Col>
          <Col md={6}>
            <h5>📝 Signup</h5>
            <Form>
              <Form.Control
                placeholder="Username"
                value={signupData.username}
                onChange={(e) => setSignupData({ ...signupData, username: e.target.value })}
                className="mb-2"
              />
              <Form.Control
                type="password"
                placeholder="Password"
                value={signupData.password}
                onChange={(e) => setSignupData({ ...signupData, password: e.target.value })}
                className="mb-2"
              />
              <Button onClick={handleSignup} disabled={loading}>
                Signup
              </Button>
            </Form>
          </Col>
        </Row>
        {loading && (
          <div className="mt-3 text-center">
            <Spinner animation="border" /> <span>Processing...</span>
          </div>
        )}
        <ToastContainer position="bottom-right" autoClose={4000} theme="colored" />
      </Container>
    );
  }

  return (
    <Container className="mt-4">
      <div className="position-relative text-center mb-4">
        <h2>🏦 Banking Dashboard</h2>
        <Button variant="warning" onClick={logout} className="position-absolute top-50 end-0 translate-middle-y">
          Logout
        </Button>
      </div>

      {loading && (
        <div className="mb-3 text-center">
          <Spinner animation="border" /> <span>Processing...</span>
        </div>
      )}

      {/* Create / Deposit / Withdraw Forms */}
      <Row className="mb-4">
        <Col md={4} className="account-card">
          <h5>👤 Create Account</h5>
          <Form>
            <Form.Group>
              <Form.Control
                placeholder="Owner Name"
                value={newAccount.ownerName}
                onChange={(e) => setNewAccount({ ...newAccount, ownerName: e.target.value })}
              />
            </Form.Group>
            <Form.Group>
              <Form.Label>Type</Form.Label>
              <Form.Select
                value={newAccount.type}
                onChange={(e) => setNewAccount({ ...newAccount, type: e.target.value })}
              >
                <option>SAVINGS</option>
                <option>CHECKING</option>
              </Form.Select>
            </Form.Group>
            <Button className="mt-2" onClick={createAccount} disabled={loading}>
              Create
            </Button>
          </Form>
        </Col>

        <Col md={4} className="account-card">
          <h5>💰 Deposit</h5>
          <Form>
            <Form.Control
              placeholder="Account ID"
              value={depositData.id}
              onChange={(e) => setDepositData({ ...depositData, id: e.target.value })}
              className="mb-2"
            />
            <Form.Control
              placeholder="Amount (₹)"
              value={depositData.amount}
              onChange={(e) => setDepositData({ ...depositData, amount: e.target.value })}
            />
            <Button className="mt-2" onClick={deposit} disabled={loading}>
              Deposit
            </Button>
          </Form>
        </Col>

        <Col md={4} className="account-card">
          <h5>💳 Withdraw</h5>
          <Form>
            <Form.Control
              placeholder="Account ID"
              value={withdrawData.id}
              onChange={(e) => setWithdrawData({ ...withdrawData, id: e.target.value })}
              className="mb-2"
            />
            <Form.Control
              placeholder="Amount (₹)"
              value={withdrawData.amount}
              onChange={(e) => setWithdrawData({ ...withdrawData, amount: e.target.value })}
            />
            <Button className="mt-2" onClick={withdraw} disabled={loading}>
              Withdraw
            </Button>
          </Form>
        </Col>
      </Row>

      {/* Transfer */}
      <Row className="mb-4">
        <Col md={6} className="account-card">
          <h5>🔄 Transfer</h5>
          <Form>
            <Form.Control
              placeholder="From Account ID"
              value={transferData.from}
              onChange={(e) => setTransferData({ ...transferData, from: e.target.value })}
              className="mb-2"
            />
            <Form.Control
              placeholder="To Account ID"
              value={transferData.to}
              onChange={(e) => setTransferData({ ...transferData, to: e.target.value })}
              className="mb-2"
            />
            <Form.Control
              placeholder="Amount (₹)"
              value={transferData.amount}
              onChange={(e) => setTransferData({ ...transferData, amount: e.target.value })}
            />
            <Button className="mt-2" onClick={transfer} disabled={loading}>
              Transfer
            </Button>
          </Form>
        </Col>
      </Row>

      {/* Accounts Table */}
      <h4 className="mb-3">📋 Accounts</h4>
      <Table bordered hover className="table-card">
        <thead className="table-gradient">
          <tr>
            <th>ID</th>
            <th>Owner</th>
            <th>Type</th>
            <th>Balance (₹)</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {accounts.map((acc) => (
            <tr key={acc.id}>
              <td>{acc.id}</td>
              <td>{acc.ownerName}</td>
              <td>{acc.type}</td>
              <td>💵 {acc.balance}</td>
              <td>
                {acc.type === "SAVINGS" && (
                  <Button
                    size="sm"
                    variant="info"
                    onClick={() => applyInterest(acc.id)}
                    disabled={loading}
                  >
                    Apply Interest ✨
                  </Button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </Table>

      <ToastContainer
        position="bottom-right"
        autoClose={4000}
        hideProgressBar={false}
        newestOnTop
        closeOnClick
        pauseOnHover
        draggable
        theme="colored"
      />
    </Container>
  );
}

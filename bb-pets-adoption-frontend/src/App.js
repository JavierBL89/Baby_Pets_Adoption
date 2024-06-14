
import { useContext } from "react";
import Register from "./components/auth/components/Register";
import Home from "./components/Home";
import Login from './components/auth/components/Login';
import PasswordReset from './components/auth/components/PasswordReset';
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import { Navigate } from "react-router-dom";
import { AuthProvider, AuthContext } from './context/AuthContext'
import VerifyAccount from "./components/auth/components/VerifyAccount";


/***
 * PrivateRoute component to protect routes that require authentication
 */
const PrivateRoute = ({ element: Component, ...rest }) => {

  const { isAuthenticated } = useContext(AuthContext);

  return (
    <Route {...rest} element={isAuthenticated ? <Component /> : <Navigate to="/login" />} />
  )
};

function App() {
  return (

    <Router>
      <AuthProvider>
        <div className="App">
          <Routes>
            <Route path="/home" element={<Home />} />
            <Route path="/register" element={<Register />} />
            <Route path="/login" element={<Login />} />
            <Route path="/auth/reset_password" element={<PasswordReset />} />
            <Route path="/verify_account" element={<VerifyAccount />} />
          </Routes>
        </div>
      </AuthProvider>
    </Router>
  );
}

export default App;

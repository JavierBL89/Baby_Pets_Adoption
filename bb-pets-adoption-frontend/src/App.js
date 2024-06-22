
import { useContext } from "react";
import Register from "./components/auth/components/Register";
import Home from "./components/Home";
import Login from './components/auth/components/Login';
import PasswordReset from './components/auth/components/PasswordReset';
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import { Navigate } from "react-router-dom";
import { AuthProvider, AuthContext } from './context/AuthContext';
import { DataPetProvider } from './context/DataPetContext';
import VerifyAccount from "./components/auth/components/VerifyAccount";
import Header from "./components/common/Header";
import Footer from "./components/common/Footer";
import DataDeletionInstructions from "./components/DataDeletionInstructions";
import PrivacyPolicy from "./components/PrivacyPolicy";
import PaymentPolicy from "./components/PaymentPolicy";


import "./css/petListing.css";
/***
 * PrivateRoute component to protect routes that require authentication
 */
const PrivateRoute = ({ element: Component, ...rest }) => {

  const { isAuthenticated } = useContext(AuthContext);

  return (
    isAuthenticated ? <Component {...rest} /> : <Navigate to="/login" />
  )
};

function App() {
  return (

    <Router>
      <AuthProvider>
        <DataPetProvider>

          <div className="App">
            <Header />
            <Routes>
              <Route path="/" element={<Home />} />

              <Route path="/register" element={<Register />} />
              <Route path="/login" element={<Login />} />
              <Route path="/auth/reset_password" element={<PasswordReset />} />
              <Route path="/verify_account" element={<VerifyAccount />} />
              <Route path="/data_deletion" element={<DataDeletionInstructions />} />
              <Route path="/privacy_policy" element={<PrivacyPolicy />} />
              <Route path="/payment_policy" element={<PaymentPolicy />} />
            </Routes>
            <Footer />
          </div>
        </DataPetProvider>

      </AuthProvider>
    </Router>
  );
}

export default App;

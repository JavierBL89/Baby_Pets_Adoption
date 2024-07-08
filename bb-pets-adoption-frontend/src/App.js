
import { useContext, useRef, useEffect } from "react";

import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import { Navigate } from "react-router-dom";

import VerifyAccount from "./components/auth/components/VerifyAccount";
import Login from './components/auth/components/Login';
import PasswordReset from './components/auth/components/PasswordReset';
import Register from "./components/auth/components/Register";

import Home from "./components/Home";
import Header from "./components/common/Header";
import Footer from "./components/common/Footer";

import DataDeletionInstructions from "./components/DataDeletionInstructions";
import PrivacyPolicy from "./components/PrivacyPolicy";
import PaymentPolicy from "./components/PaymentPolicy";

import PetDetailsView from "../src/components/pet/components/pet_view/PetDetailsView";
import ListNewPet from "../src/components/pet/components/pet_creation/ListNewPet";

import MyListings from "../src/components/management/components/listings_management/MyListings";
import MyApplications from "../src/components/management/components/applications_management/MyApplications";
import PetApplications from "../src/components/management/components/applications_management/PetApplications";
import PetUpdate from "../src/components/pet/components/pet_update/PetUpdate";

import "./css/petListing.css";
import "./css/forms.css";
import "./css/applications.css";

import { AuthProvider, AuthContext } from './context/AuthContext';
import { DataPetProvider } from "./context/DataPetContext";
import { FeedbackProvider } from "./context/FeedBackContext";


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
      <FeedbackProvider >
        <AuthProvider>
          <DataPetProvider>
            <div className="App">
              <Header />
              <Routes>
                <Route path="/:token" element={<Home />} />
                <Route path="/register" element={<Register />} />
                <Route path="/login" element={<Login />} />
                <Route path="/auth/reset_password" element={<PasswordReset />} />
                <Route path="/verify_account/:token" element={<VerifyAccount />} />

                <Route path="/pet_applications/:petId/:token" element={<PetApplications />} />
                <Route path="/my_applications/:token" element={<MyApplications />} />
                <Route path="/pets/:currentPetCategory/view/:petId" element={<PetDetailsView />} />
                <Route path="/list_new_pet/:token" element={<ListNewPet />} />
                <Route path="/update_pet/:petObjectString/:petListingId/:token" element={<PetUpdate />} />
                <Route path="/my_listings/:token" element={<MyListings />} />

                <Route path="/data_deletion" element={<DataDeletionInstructions />} />
                <Route path="/privacy_policy" element={<PrivacyPolicy />} />
                <Route path="/payment_policy" element={<PaymentPolicy />} />
              </Routes>
              <Footer />
            </div>
          </DataPetProvider>
        </AuthProvider>
      </FeedbackProvider>
    </Router>
  );
}

export default App;

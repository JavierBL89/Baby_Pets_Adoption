
import { useContext, useRef, useEffect } from "react";

import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import { Navigate } from "react-router-dom";

import VerifyAccount from "./components/auth/components/VerifyAccount";
import LoginPage from './components/auth/components/LoginPage';
import LoginHeader from './components/auth/components/LoginHeader';
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
import "./css/style.css";
import "./css/common.css";

import { AuthProvider, AuthContext } from './context/AuthContext';
import { DataPetProvider } from "./context/DataPetContext";
import { FeedbackProvider } from "./context/FeedBackContext";
import ProfileDashBoard from "./components/management/components/account_management/ProfileDashBoard";
import { NotificationsProvider } from "./context/NotificationsContext";
import ScrollToTop from "./components/common/ScrollToTop";
import About from "./components/About";
import Services from "./components/Services";
import Contact from "./components/Contact";


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
      <ScrollToTop />

      <NotificationsProvider >
        <FeedbackProvider >
          <AuthProvider>
            <DataPetProvider>
              <div className="App">
                <Header />
                <Routes>
                  <Route path="/" element={<Home />} />
                  <Route path="/:token" element={<Home />} />
                  <Route path="/register" element={<Register />} />
                  <Route path="/login" element={<LoginPage />} />
                  <Route path="/loginHeader" element={<LoginHeader />} />
                  <Route path="/auth/reset_password" element={<PasswordReset />} />
                  <Route path="/verify_account" element={<VerifyAccount />} />
                  <Route path="/profile/:token" element={<ProfileDashBoard />} />
                  <Route path="/pet_applications/:petId/:token" element={<PetApplications />} />
                  <Route path="/my_applications/:token" element={<MyApplications />} />
                  <Route path="/pets/:currentPetCategory/view/:petId" element={<PetDetailsView />} />
                  <Route path="/list_new_pet/:token" element={<ListNewPet />} />
                  <Route path="/update_pet/:petObjectString/:petListingId/:token" element={<PetUpdate />} />
                  <Route path="/my_listings/:token" element={<MyListings />} />

                  <Route path="/data_deletion" element={<DataDeletionInstructions />} />
                  <Route path="/privacy_policy" element={<PrivacyPolicy />} />
                  <Route path="/payment_policy" element={<PaymentPolicy />} />
                  <Route path="/about" element={<About />} />
                  <Route path="/services" element={<Services />} />
                  <Route path="/contact" element={<Contact />} />

                </Routes>
                <Footer />
              </div>
            </DataPetProvider>
          </AuthProvider>
        </FeedbackProvider>
      </NotificationsProvider >
    </Router>
  );
}

export default App;

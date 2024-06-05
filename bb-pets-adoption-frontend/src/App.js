
import Register from "./components/auth/components/Register";
import Home from "./components/Home";
import Login from './components/auth/components/Login';
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";

function App() {
  return (

    <Router>
      <div className="App">
        <Routes>
          <Route path="/home" element={<Home />} />
          <Route path="/register" element={<Register />} />
          <Route path="/login" element={<Login />} />
        </Routes>
      </div>

    </Router>
  );
}

export default App;

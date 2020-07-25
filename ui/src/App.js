import React, { useState } from "react";
import { BrowserRouter as Router, Link, Route } from "react-router-dom";
import PrivateRoute from './PrivateRoute';
import Home from "./pages/Home";
import Admin from "./pages/Admin";
import Signin from "./pages/Signin";
import Signup from './pages/Signup';
import { AuthContext } from "./context/Authentication";
import { Title } from './components/FormStyles';
import { Token } from './context/Token';

function App(props) {
  const [authTokens, setAuthTokens] = useState({});
  const setTokens = (data) => {
    if (data) {
      sessionStorage.setItem("token", data);
    } else {
        sessionStorage.removeItem("token");
        setAuthTokens()
    }
  }

  return (
    <AuthContext.Provider value={{ authTokens, setAuthTokens: setTokens }}>
      <Router>
        <div>
          <Title>Wylie College Course Registration System</Title>
          <hr />
        </div>
        <div>
          <PrivateRoute exact path="/" component={Home} />
          <Route path="/signin" component={Signin} />
          <Route path="/signup" component={Signup} />
          <PrivateRoute path="/admin" component={Admin} />
        </div>
      </Router>
    </AuthContext.Provider>
  );
}

export default App;
import React from "react";
import { BrowserRouter as Router, Link, Route } from "react-router-dom";
import PrivateRoute from './PrivateRoute';
import Home from "./pages/Home";
import Admin from "./pages/Admin";
import Login from "./pages/Login";
import Signup from './pages/Signup';
import { AuthContext } from "./context/auth";
import { Title } from './components/AuthForm';

//<Link to="/">Home Page</Link>
//          <Link to="/admin">Admin Page</Link>

function App(props) {
  return (
    <AuthContext.Provider value={false}>
      <Router>
        <div>
          <Title>Wylie College Course Registration System</Title>
          <hr />
        </div>
        <div>
          <PrivateRoute exact path="/" component={Home} />
          <Route path="/login" component={Login} />
          <Route path="/signup" component={Signup} />
          <PrivateRoute path="/admin" component={Admin} />
        </div>
      </Router>
    </AuthContext.Provider>
  );
}

export default App;
import React, { useState } from "react";
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import "./App.css";
import Routes from "./Routes";
import { AppContext } from "./libs/AppContext";
import { requestSignout } from "./libs/IAMRequests"
import { Token } from "./libs/Token";

function App() {
  const [isAuthenticated, userHasAuthenticated] = useState(false);
  const [token, setToken] = useState("");

  if (isAuthenticated && "" === token) {
    setToken(new Token(sessionStorage.getItem("token")));
  }

  const signoutLink =
    isAuthenticated
             ? (
               <Navbar.Collapse className="justify-content-end">
                 <Nav activeKey={window.location.pathname}>
                   <Nav.Link onClick={signout}>Signout</Nav.Link>
                 </Nav>
               </Navbar.Collapse>
               ) : null

  function signout() {
    requestSignout(() => {
      userHasAuthenticated(false)
      setToken("");
    });
  }

  return (
    <div className="App container py-3">
      <Navbar fixed="top" collapseOnSelect expand="md" className="mb-3 title">
        <Navbar.Brand>
          <h1 className="title nav-bar-textz">Wylie College Course Registration System</h1>
        </Navbar.Brand>
        {signoutLink}
      </Navbar>
      <AppContext.Provider value={{ isAuthenticated, userHasAuthenticated }}>
        <Routes token={token} />
      </AppContext.Provider>
    </div>
  );
}

export default App;

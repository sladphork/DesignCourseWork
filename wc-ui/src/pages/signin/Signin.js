import React, { useState, useRef } from "react";
import { Link, Redirect } from "react-router-dom";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import Alert from "react-bootstrap/Alert";
import "./Signin.css";
import { requestSignin } from "../../libs/IAMRequests"
import { useAppContext } from "../../libs/AppContext"

function Signin() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [isLoggedIn, setLoggedIn] = useState(false);
  const { userHasAuthenticated } = useAppContext();
  const [signinError, setSigninError] = useState(false);

  function useFocus() {
      const elem = useRef(null)
      const setFocus = () => {elem.current &&  elem.current.focus()}

      return [ elem, setFocus ]
  }

  const [inputRef, setInputFocus] = useFocus();

  function validateForm() {
    return username.length > 0 && password.length > 0;
  }

  function setToken(token) {
    sessionStorage.setItem("token", token.toString());
    userHasAuthenticated(token);
    setLoggedIn(true);
  }

  function signinFailed() {
    setUsername("");
    setPassword("");
    setSigninError(true);
    setInputFocus();
  }

  function submit(event) {
    event.preventDefault();

    requestSignin(username, password, setToken, signinFailed);
  }

  if (isLoggedIn) {
    return (
        <Redirect to="/" />
    );
  }

  const alert = signinError
                    ? <Alert variant="warning">Username and/or password are incorrect!</Alert>
                    : <span></span>
  return (
    <div className="Signin">
      <Form onSubmit={submit}>
        {alert}
        <Form.Group size="lg" controlId="email">
          <Form.Label>Username</Form.Label>
          <Form.Control
            autoFocus
            value={username}
            placeholder="Username"
            onChange={(e) => setUsername(e.target.value)}
            ref={inputRef}
          />
        </Form.Group>
        <Form.Group size="lg" controlId="password">
          <Form.Label>Password</Form.Label>
          <Form.Control
            type="password"
            value={password}
            placeholder="Password"
            onChange={(e) => setPassword(e.target.value)}
          />
        </Form.Group>
        <Button block size="lg" type="submit" disabled={!validateForm()}>
          Signin
        </Button>
      </Form>
    </div>
  );
}

export default Signin;
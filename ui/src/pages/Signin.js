import React, { useState } from "react";
import { Link, Redirect } from "react-router-dom";
import axios from 'axios';
import { Card, Logo, Form, Input, Button, Error } from "../components/FormStyles";
import { useAuth } from "../context/Authentication";
import { Token } from "../context/Token"
import { requestSignin } from "../context/IamRequests"

function Signin() {
  const [isLoggedIn, setLoggedIn] = useState(false);
  const [isError, setIsError] = useState(false);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const { setAuthTokens } = useAuth();

  function setToken(token) {
    setAuthTokens(token)
    setLoggedIn(true)
  }

  function setError() {
    setUsername("")
    setPassword("")
    setIsError(true)
  }

  function signin() {
    requestSignin(username, password, setToken, setError)
  }

  if (isLoggedIn) {
    return <Redirect to="/" />;
  }

// TODO: Logo can be moved to be the first line after <Card>
//<Logo src={logoImg} />
  return (
    <Card>

      <Form>
        <Input
          type="username"
          value={username}
          onChange={e => {
            setUsername(e.target.value);
          }}
          placeholder="username"
        />
        <Input
          type="password"
          value={password}
          onChange={e => {
            setPassword(e.target.value);
          }}
          placeholder="password"
        />
        <Button onClick={signin}>Sign In</Button>
      </Form>
        { isError &&<Error>The username and/or password provided were incorrect!</Error> }
    </Card>
  );
}

export default Signin;
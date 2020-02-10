import React, { useState } from "react";
import { Link, Redirect } from "react-router-dom";
import axios from 'axios';
//import logoImg from "../img/logo.jpg";
import { Card, Logo, Form, Input, Button, Error } from "../components/AuthForm";
import { useAuth } from "../context/auth";

function Login() {
  const [isLoggedIn, setLoggedIn] = useState(false);
  const [isError, setIsError] = useState(false);
  const [userName, setUserName] = useState("");
  const [password, setPassword] = useState("");
  const { setAuthTokens } = useAuth();

  function postLogin() {
    axios.post("https://www.somePlace.com/auth/login", {
      userName,
      password
    }).then(result => {
      if (result.status === 200) {
        setAuthTokens(result.data);
        setLoggedIn(true);
      } else {
        setIsError(true);
      }
    }).catch(e => {
      setIsError(true);
    });
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
          value={userName}
          onChange={e => {
            setUserName(e.target.value);
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
        <Button onClick={postLogin}>Sign In</Button>
      </Form>
        { isError &&<Error>The username or password provided were incorrect!</Error> }
    </Card>
  );
}

export default Login;
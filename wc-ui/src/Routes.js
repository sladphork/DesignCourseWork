import React from "react";
import { Route, Switch } from "react-router-dom";
import AuthenticatedRoute from "./components/AuthenticatedRoute";
import Home from "./pages/Home";
import Signin from "./pages/signin/Signin";

export default function Routes(props) {

  return (
    <Switch>
      <AuthenticatedRoute exact path="/">
        <Home {...props} />
      </AuthenticatedRoute>
      <Route exact path="/signin">
        <Signin />
      </Route>
    </Switch>
  );
}
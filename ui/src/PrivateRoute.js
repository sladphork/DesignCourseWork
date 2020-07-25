import React from "react";
import { Route, Redirect } from "react-router-dom";
import { useAuth } from "./context/Authentication";

// TODO: I think we'll rename the auth stuff to session or something.
//  This way we can make it a bit smarter.
function PrivateRoute({ component: Component, ...rest }) {
  const { authTokens } = useAuth();

  return (
    <Route
      {...rest}
      render={props =>
        authTokens ? (
          <Component {...props} />
        ) : (
          <Redirect to="/signin" />
        )
      }
    />
  );
}

export default PrivateRoute;
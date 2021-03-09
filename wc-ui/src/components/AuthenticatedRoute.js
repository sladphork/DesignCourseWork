import React from "react";
import { Route, Redirect } from "react-router-dom";
import { useAppContext } from "../libs/AppContext";

export default function AuthenticatedRoute({ children, ...rest }) {
  const { isAuthenticated } = useAppContext();
  return (
    <Route {...rest}>
      {isAuthenticated ? (
        children
      ) : (
        <Redirect to={
          "/signin"
        } />
      )}
    </Route>
  );
}
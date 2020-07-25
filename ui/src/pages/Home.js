import React from "react";
import { useAuth } from "../context/Authentication";
import { Button } from "../components/FormStyles";
import { requestSignout } from "../context/IamRequests"

function Home(props) {
 const { setAuthTokens } = useAuth();

  function signout() {
    requestSignout(setAuthTokens);
  }

  return (
    <div>
      <div>Home Page</div>
      <Button onClick={signout}>Sign Out</Button>
    </div>
  );
}

export default Home;
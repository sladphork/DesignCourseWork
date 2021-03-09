import React from "react";
import "./Home.css";
import Registrar from "./registrar/Registrar";

export default function Home(props) {

  function page() {
    switch(props.token.role) {
      case "REGISTRAR":
        return (
          <div className="lander">
            <Registrar />
          </div>
        );
      default:
        return (
          <div className="lander">
            <h1>Scratch</h1>
            <p className="text-muted">A simple note taking app</p>
          </div>
        );
    }
  }

  return (
    <div className="Home">
      { page() }
    </div>
  );
}
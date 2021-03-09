import React from "react";
import "./Professors.css";
import ProfessorTable from "./ProfessorTable.js";

export default function Professors(props) {

  return (
    <div className="Professors">
      <ProfessorTable
        professors={props.professors}
        departments={props.departments}
        doAdd={props.doAdd}
        doUpdate={props.doUpdate}
        doDelete={props.doDelete} />
    </div>
  );
}
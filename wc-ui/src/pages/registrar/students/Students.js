import React from "react";
import "./Students.css";
import StudentTable from "./StudentTable.js";

export default function Students(props) {

  return (
    <div className="Students">
      <StudentTable
        students={props.students}
        faculties={props.faculties}
        doAdd={props.doAdd}
        doUpdate={props.doUpdate}
        doDelete={props.doDelete} />
    </div>
  );
}
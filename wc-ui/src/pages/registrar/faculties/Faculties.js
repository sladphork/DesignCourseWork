import React from 'react';
import "./Faculties.css";

export default function Faculties(props) {

  function mapDepartments(faculty) {
    return faculty.departments.map(dept => {
      return (
        <dd>{dept.name}</dd>
      );
    });
  }

  function map() {
    return props.faculties.map(faculty => {
      return (
        <React.Fragment>
        <dt>{faculty.name}</dt>
        {mapDepartments(faculty)}
        </React.Fragment>
      )
    });
  }

  return (
    <div className="Faculties">
      <dl>
        {map()}
      </dl>
    </div>
  );
}
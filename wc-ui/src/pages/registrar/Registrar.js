import React, { useState, useEffect } from "react";
import "./Registrar.css";
import { Tab, Tabs } from 'react-bootstrap';
import Professors from "./professors/Professors";
import Students from "./students/Students";
import Faculties from "./faculties/Faculties";
import { getProfessors, addProfessor, updateProfessor, deleteProfessor } from "../../libs/ProfessorRequests";
import { getStudents, addStudent, updateStudent, deleteStudent } from "../../libs/StudentRequests";
import { getFaculties } from "../../libs/FacultyRequests";

export default function Registrar() {

  const [loading, setLoading] = useState(true);
  const [professors, setProfessors] = useState([]);
  const [faculties, setFaculties] = useState([]);
  const [departments, setDepartments] = useState({});
  const [students, setStudents] = useState([]);

  function load() {
    getProfessors(
        data => setProfessors(data)
    );
    getStudents(
        data => setStudents(data)
    );

    getFaculties(
      data => {
        setFaculties(data);

        const depts = data.flatMap(faculty => faculty.departments)
          .flatMap(dept => dept)
        const reduced = depts.reduce((r,e) => {
            r[e.id] = e.name;
            return r;
        }, {});
        setDepartments(reduced);
      }
    );

//    setLoading(false);
  }

  function doAdd(newProfessor) {
    console.log(newProfessor);
    addProfessor(
        newProfessor,
        () => load()
    );
  }

  function doUpdate(updatedProfessor) {
    updateProfessor(
      updatedProfessor,
      () => load()
    );
  }

  function doDelete(deletedProfessor) {
    deleteProfessor(
      deletedProfessor,
      () => load()
    );
  }

  function doAddStudent(newStudent) {
    addStudent(
        newStudent,
        () => load()
    );
  }

  function doUpdateStudent(updatedStudent) {
    updateStudent(
      updatedStudent,
      () => load()
    );
  }

  function doDeleteStudent(deletedStudent) {
    deleteStudent(
      deletedStudent,
      () => load()
    );
  }

  useEffect(() => {
    load();
  }, []);

  if (Object.keys(departments).length === 0) {
    return (
      <div className="Registrar">
        Loading...<br />
        Please Wait!
      </div>
    );
  }
  return (
    <div className="Registrar">
      <Tabs defaultActiveKey="professors" id="uncontrolled-tab-example">
        <Tab eventKey="professors" title="Professors">
          <Professors
            professors={professors}
            departments={departments}
            doAdd={doAdd}
            doUpdate={doUpdate}
            doDelete={doDelete}
          />
        </Tab>
        <Tab eventKey="students" title="Students">
          <Students
            students={students}
            faculties={faculties}
            doAdd={doAddStudent}
            doUpdate={doUpdateStudent}
            doDelete={doDeleteStudent}
          />
        </Tab>
        <Tab eventKey="faculties" title="Faculties">
          <Faculties
            faculties={faculties}
          />
        </Tab>
      </Tabs>
    </div>
  );
}
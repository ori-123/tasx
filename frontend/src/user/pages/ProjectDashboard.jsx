import React, {useEffect, useState} from "react";
import useAuthFetch from "../../api/useAuthFetch.js";
import LoadingSpinner from "../../components/LoadingSpinner.jsx";
import {Link, useParams} from "react-router-dom";
import BackButton from "../../components/BackButton.jsx";
import useAuth from "../../auth/hooks/useAuth.js";
import {Alert, Container, Paper, Snackbar, Typography,} from "@mui/material";
import {styled} from "@mui/system";
import {format, parseISO} from "date-fns";

const StyledPaper = styled(Paper)(({theme}) => ({
  padding: theme.spacing(4),
  borderRadius: theme.spacing(2),
  background: "linear-gradient(45deg, #516d79 30%, #11508d 90%)",
  color: "#FFFFFF",
  accentColor: "#FFFFFF",
}));

const StyledTypography = styled(Typography)(({theme}) => ({
  fontWeight: "bold",
  fontSize: "2rem",
  color: "#FFFFFF",
  marginBottom: theme.spacing(2),
}));

const StyledSubtitle = styled(Typography)(({theme}) => ({
  fontSize: "1.2rem",
  color: "#FFFFFF",
  marginBottom: theme.spacing(2),
}));

function ProjectDashboard() {
  const authFetch = useAuthFetch();
  const {auth} = useAuth();

  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState(null);
  const companyId = useParams()?.companyId;
  const projectId = useParams()?.projectId;
  const [project, setProject] = useState(null);
  const [unfinishedTasks, setUnfinishedTasks] = useState(null)
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");
  const [snackbarSeverity, setSnackbarSeverity] = useState("error");

  const userIsProjectOwner =
    project?.projectOwner?.username &&
    auth?.username &&
    project.projectOwner.username === auth.username;

  async function loadProject() {
    try {
      setLoading(true);
      const {httpResponse, responseObject} = await authFetch(
        "GET",
        `companies/${companyId}/projects/${projectId}`
      );
      if (httpResponse?.status !== 200 || !responseObject?.data) {
        setErrorMessage(responseObject?.error ?? "Failed to load project");
        setProject(null);
        return;
      }
      setProject(responseObject.data);
    } catch (e) {
      console.error(e);
      setErrorMessage("Failed to load project");
      setProject(null);
    } finally {
      setLoading(false);
    }
  }

  async function loadUnfinishedTasks() {
    try {
      setLoading(true);
      const {httpResponse, responseObject} = await authFetch(
          "GET",
          `companies/${companyId}/projects/${projectId}/tasks/unfinishedtasks`
      );
      if (httpResponse?.status !== 200 || !responseObject?.data) {
        setErrorMessage(responseObject?.error ?? "Failed to load tasks");
        setUnfinishedTasks(null);
        return;
      }
      setUnfinishedTasks(responseObject.data);
    } catch (e) {
      console.error(e);
      setErrorMessage("Failed to load tasks");
      setUnfinishedTasks(null);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadProject();
    loadUnfinishedTasks();
  }, []);

  return loading ? (
    <LoadingSpinner/>
  ) : (
    <Container>
      <Snackbar
        open={snackbarOpen}
        autoHideDuration={6000}
        onClose={() => setSnackbarOpen(false)}
        anchorOrigin={{vertical: "top", horizontal: "center"}}
      >
        <Alert
          onClose={() => setSnackbarOpen(false)}
          severity={snackbarSeverity}
        >
          {snackbarMessage}
        </Alert>
      </Snackbar>
      <StyledPaper>
        {errorMessage && (
          <Typography component="h5" variant="h6" color="error">
            {errorMessage}
          </Typography>
        )}

        {project ? (
          <div className={"projectDetails"}>
            <StyledTypography variant="h2">{project.name}</StyledTypography>

            {!userIsProjectOwner ? (
              <Typography variant="h5">
                Project owner: {project.projectOwner?.username}
              </Typography>
            ) : (
              <Typography variant="h5">You own this project</Typography>
            )}

            {project.assignedEmployees?.length ? (
              <>
                <StyledSubtitle>Assigned employees:</StyledSubtitle>
                <ul className={"employeeList"}>
                  {project.assignedEmployees.map((employee) => {
                    return <li key={employee.userId}>{employee.username}</li>;
                  })}
                </ul>
              </>
            ) : (
              <></>
            )}

            <StyledSubtitle>Tasks:</StyledSubtitle>
            <ul className={"projectList"}>
              {unfinishedTasks?.length ? (
                unfinishedTasks.map((task) => {
                  return (
                    <li key={task.taskId}>
                      <Link
                        to={`/companies/${companyId}/projects/${projectId}/tasks/${task.taskId}`}
                        style={{color: "#FFFFFF"}}
                      >
                        {task.name}
                      </Link>
                      <p>{task.description}</p>
                    </li>
                  );
                })
              ) : (
                <p>
                  No tasks found
                </p>
              )}
              <Link to={`/companies/${companyId}/projects/${projectId}/tasks/create`} style={{color: "#FFFFFF"}}>
                Create a new task
              </Link>
            </ul>


            <Typography variant="h6">
              Start date: {format(parseISO(project.startDate), "yyyy-MM-dd HH:mm")}
            </Typography>
            <Typography variant="h6">
              Deadline: {format(parseISO(project.deadline), "yyyy-MM-dd HH:mm")}
            </Typography>

            {userIsProjectOwner ? (
              <>
                <Typography variant="h3" sx={{marginTop: 3}}>
                  <Link
                    to={`/companies/${companyId}/projects/update/${projectId}`}
                    style={{color: "#FFFFFF"}}
                  >
                    Update project details
                  </Link>
                </Typography>
              </>
            ) : (
              <></>
            )}
          </div>
        ) : (
          <></>
        )}
        <BackButton path={`/companies/${companyId}`}/>
      </StyledPaper>
    </Container>
  );
}

export default ProjectDashboard;

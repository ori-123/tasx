import React, {useEffect, useState} from "react";
import useAuthFetch from "../../api/useAuthFetch.js";
import LoadingSpinner from "../../components/LoadingSpinner.jsx";
import {Link} from "react-router-dom";
import {
  Button, Container, Grid, Paper, Typography, Snackbar, Alert
} from "@mui/material";
import {styled} from "@mui/system";

const StyledPaper = styled(Paper)(({theme}) => ({
  padding: theme.spacing(4),
  borderRadius: theme.spacing(2),
  background: "linear-gradient(45deg, #516d79 30%, #11508d 90%)",
  color: "#FFFFFF",
  accentColor: "#FFFFFF"
}));

const StyledTypography = styled(Typography)(({theme}) => ({
  fontWeight: "bold", fontSize: "2rem", color: "#FFFFFF", marginBottom: theme.spacing(2)
}));

function UserDashboard() {
  const authFetch = useAuthFetch();

  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState(null);
  const [joinedCompanies, setJoinedCompanies] = useState([]);
  const [companiesToJoin, setCompaniesToJoin] = useState([]);
  const [joinRequests, setJoinRequests] = useState([]);
  const [projectJoinRequests, setProjectJoinRequests] = useState([]);
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");
  const [snackbarSeverity, setSnackbarSeverity] = useState("error");

  async function loadJoinRequests() {
    try {
      setLoading(true);
      const {httpResponse, responseObject} = await authFetch("GET", `user/requests`);
      if (httpResponse?.status !== 200 || !responseObject?.data) {
        throw new Error(responseObject?.error ?? "Failed to load join requests");
      }
      setJoinRequests(responseObject.data);
    } catch (e) {
      console.error(e);
      setJoinRequests([]);
      setSnackbarSeverity("error");
      setSnackbarMessage(e.message ?? "Failed to load join requests");
      setSnackbarOpen(true);
    } finally {
      setLoading(false);
    }
  }

  async function loadProjectJoinRequests() {
    try {
      setLoading(true);
      const {httpResponse, responseObject} = await authFetch("GET", `user/project-requests`);
      if (httpResponse?.status !== 200 || !responseObject?.data) {
        throw new Error(responseObject?.error ?? "Failed to load project join requests");
      }
      setProjectJoinRequests(responseObject.data);
    } catch (e) {
      console.error(e);
      setProjectJoinRequests([]);
      setSnackbarSeverity("error");
      setSnackbarMessage("Failed to load project join requests");
      setSnackbarOpen(true);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadJoinRequests();
    loadProjectJoinRequests();
  }, []);


  return loading ? (<LoadingSpinner/>) : (<Container>
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
      <Grid container spacing={4} justifyContent="center">
        <Grid item xs={12} md={12} lg={12}>
          <StyledPaper>
            {joinRequests?.length ? (<>
                <StyledTypography variant="h2">Company join requests</StyledTypography>
                <ul className={"joinRequestList"}>
                  {joinRequests.map((request) => {
                    return (<li className={"joinRequestItem"} key={request?.requestId}>
                        <p>
                          <strong>{request.company.name}</strong>{" - "}
                          {request.status}
                        </p>
                      </li>);
                  })}
                </ul>
              </>) : (<></>)}

            {projectJoinRequests?.length ? (<>
                <StyledTypography variant="h2">Project join requests</StyledTypography>
                <ul className={"joinRequestList"}>
                  {projectJoinRequests.map((request) => {
                    return (<li className={"joinRequestItem"} key={request?.requestId}>
                        <p>
                          <strong>{request.company.name}</strong>{" - "}
                          {request.status}
                        </p>
                      </li>);
                  })}
                </ul>
              </>) : (<></>)}
          </StyledPaper>
        </Grid>
      </Grid>
    </Container>);
}

export default UserDashboard;

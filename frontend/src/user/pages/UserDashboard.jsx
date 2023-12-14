import React, { useEffect, useState } from "react";
import useAuthFetch from "../../api/useAuthFetch.js";
import LoadingSpinner from "../../components/LoadingSpinner.jsx";
import { Link } from "react-router-dom";
import {
  Button,
  Container,
  Grid,
  Paper,
  Typography,
  Snackbar,
  Alert,
} from "@mui/material";
import { styled } from "@mui/system";

const StyledPaper = styled(Paper)(({ theme }) => ({
  padding: theme.spacing(4),
  borderRadius: theme.spacing(2),
  background: "linear-gradient(45deg, #516d79 30%, #11508d 90%)",
  color: "#FFFFFF",
  accentColor: "#FFFFFF",
}));

const StyledTypography = styled(Typography)(({ theme }) => ({
  fontWeight: "bold",
  fontSize: "2rem",
  color: "#FFFFFF",
  marginBottom: theme.spacing(2),
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

  async function loadJoinedCompanies() {
    try {
      setLoading(true);
      const { httpResponse, responseObject } = await authFetch(
          "GET",
          "companies?withUser=true"
      );
      if (httpResponse?.status !== 200 || !responseObject?.data) {
        setErrorMessage(
            responseObject?.error ?? "Failed to load joined companies"
        );
        setJoinedCompanies([]);
        return;
      }
      console.log(responseObject.data);
      setJoinedCompanies(responseObject.data);
    } catch (e) {
      console.error(e);
      setErrorMessage("Failed to load joined companies");
      setJoinedCompanies([]);
    } finally {
      setLoading(false);
    }
  }

  async function loadCompaniesToJoin() {
    try {
      setLoading(true);
      const { httpResponse, responseObject } = await authFetch(
          "GET",
          "companies?withUser=false"
      );
      if (httpResponse?.status !== 200 || !responseObject?.data) {
        setErrorMessage(
            responseObject?.error ?? "Failed to load companies to join"
        );
        setCompaniesToJoin([]);
        return;
      }
      console.log(responseObject.data);
      setCompaniesToJoin(responseObject.data);
    } catch (e) {
      console.error(e);
      setErrorMessage("Failed to load companies to join");
      setCompaniesToJoin([]);
    } finally {
      setLoading(false);
    }
  }

  async function loadJoinRequests() {
    try {
      setLoading(true);
      const { httpResponse, responseObject } = await authFetch(
          "GET",
          `user/requests`
      );
      if (httpResponse?.status !== 200 || !responseObject?.data) {
        setErrorMessage(
            responseObject?.error ?? "Failed to load join requests"
        );
        setCompaniesToJoin([]);
        return;
      }
      console.log(responseObject.data);
      setJoinRequests(responseObject.data);
    } catch (e) {
      console.error(e);
      setErrorMessage("Failed to load join requests");
      setJoinRequests([]);
    } finally {
      setLoading(false);
    }
  }

  async function loadProjectJoinRequests() {
    try {
      setLoading(true);
      const { httpResponse, responseObject } = await authFetch(
          "GET",
          `user/project_requests`
      );
      if (httpResponse?.status !== 200 || !responseObject?.data) {
        setErrorMessage(
            responseObject?.error ?? "Failed to load project joint requests"
        );
        return;
      }
    } catch (e) {
      console.error(e);
      setErrorMessage("Failed to load project join requests");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadJoinedCompanies();
    loadCompaniesToJoin();
    loadJoinRequests();
    loadProjectJoinRequests();
  }, []);

  async function handleJoinCompany(companyId) {
    try {
      setLoading(true);
      const { httpResponse, responseObject } = await authFetch(
          "POST",
          `companies/${companyId}/requests`
      );
      console.log(httpResponse, responseObject);
      if (httpResponse?.status !== 201 || !responseObject?.data) {
        setSnackbarSeverity("error");
        setSnackbarMessage(
            responseObject?.error ?? "Failed to process join request"
        );
        setSnackbarOpen(true);
      } else {
        setSnackbarSeverity("success");
        const companyName = responseObject?.data?.company?.name;
        const joinRequestStatus = responseObject?.data?.status;
        if (!companyName || !joinRequestStatus) {
          throw new Error("Invalid response received for join request");
        }
        await loadCompaniesToJoin();
        await loadJoinRequests();
        setSnackbarMessage(
            `Request to join ${companyName} sent successfully`
        );
        setSnackbarOpen(true);
      }
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  }

  return loading ? (
      <LoadingSpinner />
  ) : (
      <Container>
        <Snackbar
            open={snackbarOpen}
            autoHideDuration={6000}
            onClose={() => setSnackbarOpen(false)}
            anchorOrigin={{ vertical: "top", horizontal: "center" }}
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
              {errorMessage && (
                  <Typography component="h5" variant="h6" color="error">
                    {errorMessage}
                  </Typography>
              )}

              <StyledTypography variant="h2">Your companies</StyledTypography>
              {joinedCompanies?.length ? (
                  <ul className={"companyList"}>
                    {joinedCompanies.map((company) => {
                      return (
                          <li className={"companyItem"} key={company?.companyId}>
                            <Link
                                to={`/companies/${company.companyId}`}
                                style={{ color: "#FFFFFF" }}
                            >
                              {company.name}
                            </Link>
                            <p>Description: {company.description}</p>
                          </li>
                      );
                    })}
                  </ul>
              ) : (
                  <div>
                    <p>No companies found</p>
                  </div>
              )}

              <StyledTypography variant="h2">Companies to join</StyledTypography>
              {companiesToJoin?.length ? (
                  <ul className={"companyList"}>
                    {companiesToJoin.map((company) => {
                      return (
                          <li className={"companyItem"} key={company?.comapnyId}>
                            <Button variant={"contained"}
                                    onClick={() => {
                                      handleJoinCompany(company.companyId);
                                    }}
                                    style={{ color: "#FFFFFF" }}
                            >
                              {company.name}
                            </Button>
                            <p>Description: {company.description}</p>
                          </li>
                      );
                    })}
                  </ul>
              ) : (
                  <p>No companies to join</p>
              )}

              <Link to={"/companies/create"} style={{ color: "#FFFFFF" }}>
                <h3>Create new company</h3>
              </Link>

              {joinRequests?.length ? (
                  <>
                    <StyledTypography variant="h2">Company Join Requests</StyledTypography>
                    <ul className={"joinRequestList"}>
                      {joinRequests.map((request) => {
                        return (
                            <li className={"joinRequestItem"} key={request?.requestId}>
                              <p>
                                Join company <strong>{request.company.name}</strong> -{" "}
                                {request.status}
                              </p>
                            </li>
                        );
                      })}
                    </ul>
                  </>
              ) : (
                  <></>
              )}

              {projectJoinRequests?.length ? (
                  <>
                    <StyledTypography variant="h2">Project Join Requests</StyledTypography>
                    <ul className={"joinRequestList"}>
                      {projectJoinRequests.map((request) => {
                        return (
                            <li className={"joinRequestItem"} key={request?.requestId}>
                              <p>
                                Join project <strong>{request.project.name}</strong> -{" "}
                                {request.status}
                              </p>
                            </li>
                        );
                      })}
                    </ul>
                  </>
              ) : (
                  <></>
              )}
            </StyledPaper>
          </Grid>
        </Grid>
      </Container>
  );
}

export default UserDashboard;

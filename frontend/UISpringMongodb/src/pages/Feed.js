import {
  Box,
  Card,
  Grid,
  TextField,
  Typography,
  InputAdornment,
  Button,
} from "@mui/material";

import axios from "axios";
import React, { useEffect, useState } from "react";

import SearchIcon from "@mui/icons-material/Search";
import { Link } from "react-router-dom";

const Feed = () => {

  const [query, setQuery] = useState("");
  const [post, setPost] = useState([]);

  useEffect(() => {

    const fetchInitialPosts = async () => {

      try {

        const response = await axios.get(
          "http://localhost:8080/api/job"
        );

        console.log("All jobs:", response.data);

        setPost(response.data);

      } catch (error) {

        console.error(
          "Error fetching jobs:",
          error
        );

      }

    };

    const fetchPosts = async () => {

      try {

        const response = await axios.get(
          `http://localhost:8080/api/job/${query}`
        );

        console.log("Search result:", response.data);

        setPost(response.data);

      } catch (error) {

        console.error(
          "Error searching jobs:",
          error
        );

      }

    };

    if (query.length === 0) {

      fetchInitialPosts();

    } else if (query.length > 2) {

      fetchPosts();

    }

  }, [query]);


  return (
    <Grid
      container
      spacing={2}
      sx={{ margin: "2%" }}
    >

      {/* Home button + Search */}

      <Grid
        item
        xs={12}
        md={12}
        lg={12}
      >

        <Button
          sx={{ margin: "1% 2%" }}
          variant="outlined"
        >
          <Link to="/">
            Home
          </Link>
        </Button>

        <Box>

          <TextField
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <SearchIcon />
                </InputAdornment>
              ),
            }}

            placeholder="Search..."

            sx={{
              width: "75%",
              padding: "2% auto",
            }}

            fullWidth

            value={query}

            onChange={(e) =>
              setQuery(e.target.value)
            }
          />

        </Box>

      </Grid>


      {/* Job Cards */}

      {post.map((p) => {

        return (

          <Grid
            key={p.id}
            item
            xs={12}
            md={6}
            lg={4}
          >

            <Card
              sx={{
                padding: "3%",
                overflow: "hidden",
                width: "84%",
              }}
            >

              <Typography
                variant="h5"
                sx={{
                  fontSize: "2rem",
                  fontWeight: "600",
                }}
              >
                {p.profile}
              </Typography>


              <Typography
                sx={{
                  color: "#585858",
                  marginTop: "2%",
                }}
                variant="body1"
              >
                Description: {p.desc}
              </Typography>


              <br />
              <br />


              <Typography variant="h6">
                Years of Experience: {p.exp} years
              </Typography>


              <Typography
                gutterBottom
                variant="body1"
              >
                Skills:
              </Typography>


              {/* IMPORTANT:
                  Your Java entity uses "tech",
                  not "techs"
              */}

              {p.tech &&
                p.tech.map((skill, index) => {

                  return (

                    <Typography
                      variant="body1"
                      gutterBottom
                      key={index}
                    >
                      {skill}
                    </Typography>

                  );

                })}

            </Card>

          </Grid>

        );

      })}

    </Grid>
  );
};

export default Feed;

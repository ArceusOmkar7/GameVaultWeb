<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script>
  // Add Game Modal
  const addGameBtn = document.getElementById("addGameBtn");
  const addGameModal = document.getElementById("addGameModal");
  const closeAddGameModal = document.getElementById("closeAddGameModal");
  const cancelAddGame = document.getElementById("cancelAddGame");
  const addGameForm = document.getElementById("addGameForm");

  addGameBtn.addEventListener("click", () => {
    addGameModal.classList.remove("hidden");
    // Initialize rating slider display
    document.getElementById("ratingValue").textContent =
      document.getElementById("rating").value;
  });

  closeAddGameModal.addEventListener("click", () => {
    addGameModal.classList.add("hidden");
  });

  cancelAddGame.addEventListener("click", () => {
    addGameModal.classList.add("hidden");
  }); // Add Game Form Submission
  if (addGameForm) {
    addGameForm.addEventListener("submit", function (e) {
      console.log("Form submission started");
      // Prevent default behavior initially
      e.preventDefault();

      // Validate form
      const title = this.querySelector("#title").value.trim();
      const description = this.querySelector("#description").value.trim();
      const developer = this.querySelector("#developer").value.trim();
      const price = this.querySelector("#price").value.trim();
      const releaseDate = this.querySelector("#releaseDate").value.trim();
      const platformSelect = this.querySelector("#platform");
      const genreSelect = this.querySelector("#genre");

      // Get selected platforms and genres
      const selectedPlatforms = Array.from(platformSelect.selectedOptions).map(
        (option) => option.value
      );
      const selectedGenres = Array.from(genreSelect.selectedOptions).map(
        (option) => option.value
      );

      console.log("Selected platforms:", selectedPlatforms);
      console.log("Selected genres:", selectedGenres);

      // Validation
      let isValid = true;
      const errorMessages = [];

      if (!title) {
        errorMessages.push("Title is required");
        isValid = false;
      }

      if (!description) {
        errorMessages.push("Description is required");
        isValid = false;
      }

      if (!developer) {
        errorMessages.push("Developer is required");
        isValid = false;
      }

      if (!price) {
        errorMessages.push("Price is required");
        isValid = false;
      }

      if (!releaseDate) {
        errorMessages.push("Release date is required");
        isValid = false;
      }

      if (selectedPlatforms.length === 0) {
        errorMessages.push("Please select at least one platform");
        isValid = false;
      }

      if (selectedGenres.length === 0) {
        errorMessages.push("Please select at least one genre");
        isValid = false;
      }

      if (!isValid) {
        alert(
          "Please correct the following errors:\n" + errorMessages.join("\n")
        );
        return;
      }

      console.log("Form validation passed, submitting form manually");

      // Create a new form that will be submitted
      const manualForm = document.createElement("form");
      manualForm.method = "post";
      manualForm.action = this.action;
      manualForm.style.display = "none";

      // Add the standard fields
      const addField = (name, value) => {
        const input = document.createElement("input");
        input.type = "hidden";
        input.name = name;
        input.value = value;
        manualForm.appendChild(input);
      };

      // Add all the basic form fields
      addField("title", title);
      addField("description", description);
      addField("developer", developer);
      addField("price", price);
      addField("releaseDate", releaseDate);

      // Add the image path if present
      const imagePath = this.querySelector("#imagePath").value;
      if (imagePath) {
        addField("imagePath", imagePath);
      }

      // Add the rating if present
      const rating = this.querySelector("#rating").value;
      if (rating) {
        addField("rating", rating);
      }

      // Add each selected platform as a separate field
      selectedPlatforms.forEach((platform) => {
        addField("platform", platform);
      });

      // Add each selected genre as a separate field
      selectedGenres.forEach((genre) => {
        addField("genre", genre);
      });

      // Add the form to the document and submit it
      document.body.appendChild(manualForm);
      console.log("Submitting manual form with fields:", manualForm.elements);
      manualForm.submit();
    });
  }

  // Delete Game Modal
  const deleteModal = document.getElementById("deleteModal");
  const cancelDelete = document.getElementById("cancelDelete");
  const deleteButtons = document.querySelectorAll(".delete-game");
  const deleteGameTitle = document.getElementById("deleteGameTitle");
  const deleteGameId = document.getElementById("deleteGameId");

  deleteButtons.forEach((button) => {
    button.addEventListener("click", (e) => {
      e.preventDefault();
      const gameId = button.getAttribute("data-id");
      const gameTitle = button.getAttribute("data-title");

      deleteGameId.value = gameId;
      deleteGameTitle.textContent = gameTitle;
      deleteModal.classList.remove("hidden");
    });
  });

  cancelDelete.addEventListener("click", () => {
    deleteModal.classList.add("hidden");
  });

  // Edit Game Modal
  const editModal = document.getElementById("editGameModal");
  const closeEditGameModal = document.getElementById("closeEditGameModal");
  const cancelEditGame = document.getElementById("cancelEditGame");
  const editButtons = document.querySelectorAll(".edit-game-btn");

  // Function to format date to YYYY-MM-DD for input[type="date"]
  function formatDateForInput(dateString) {
    if (!dateString) return "";
    const date = new Date(dateString);
    if (isNaN(date.getTime())) return "";

    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");
    return `${year}-${month}-${day}`;
  }

  // Function to load game data for editing
  async function loadGameForEdit(gameId) {
    try {
      const response = await fetch(
        `${pageContext.request.contextPath}/admin/edit-game?id=${gameId}&ajax=true`
      );
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }

      const data = await response.json();
      const game = data.game || data; // Backwards compatibility for older format
      const platforms = data.platforms || [];
      const genres = data.genres || [];

      // Populate form fields
      document.getElementById("editGameId").value = game.gameId;
      document.getElementById("editTitle").value = game.title || "";
      document.getElementById("editDescription").value = game.description || "";
      document.getElementById("editDeveloper").value = game.developer || "";
      document.getElementById("editPrice").value = game.price || ""; // Populate platform dropdown
      const platformSelect = document.getElementById("editPlatform");
      // First clear existing options
      while (platformSelect.options.length > 0) {
        platformSelect.remove(0);
      }

      // Parse platforms from game (could be comma-separated)
      const gamePlatforms = game.platform ? game.platform.split(", ") : [];

      // Add new options
      platforms.forEach((platform) => {
        const option = document.createElement("option");
        option.value = platform.name;
        option.textContent = platform.name;
        // Check if this platform is in the game's platforms
        if (gamePlatforms.includes(platform.name)) {
          option.selected = true;
        }
        platformSelect.appendChild(option);
      });

      // If we have platform values but no matching options, add them
      gamePlatforms.forEach((platformName) => {
        if (
          !Array.from(platformSelect.options).some(
            (opt) => opt.value === platformName
          )
        ) {
          const option = document.createElement("option");
          option.value = platformName;
          option.textContent = platformName;
          option.selected = true;
          platformSelect.appendChild(option);
        }
      });

      // Populate genre dropdown
      const genreSelect = document.getElementById("editGenre");
      // First clear existing options
      while (genreSelect.options.length > 0) {
        genreSelect.remove(0);
      }

      // Parse genres from game (could be comma-separated)
      const gameGenres = game.genre ? game.genre.split(", ") : [];

      // Add new options
      genres.forEach((genre) => {
        const option = document.createElement("option");
        option.value = genre.name;
        option.textContent = genre.name;
        // Check if this genre is in the game's genres
        if (gameGenres.includes(genre.name)) {
          option.selected = true;
        }
        genreSelect.appendChild(option);
      });

      // If we have genre values but no matching options, add them
      gameGenres.forEach((genreName) => {
        if (
          !Array.from(genreSelect.options).some(
            (opt) => opt.value === genreName
          )
        ) {
          const option = document.createElement("option");
          option.value = genreName;
          option.textContent = genreName;
          option.selected = true;
          genreSelect.appendChild(option);
        }
      });

      // Set rating slider and display value
      const editRating = document.getElementById("editRating");
      editRating.value = game.rating || 0;
      document.getElementById("editRatingValue").textContent = editRating.value;

      document.getElementById("editImagePath").value = game.imagePath || "";

      // Handle release date (convert to YYYY-MM-DD)
      document.getElementById("editReleaseDate").value = formatDateForInput(
        game.releaseDate
      );

      // Display current image if available
      const currentImageContainer = document.getElementById(
        "currentImageContainer"
      );
      const currentGameImage = document.getElementById("currentGameImage");

      if (game.imagePath) {
        currentGameImage.src = game.imagePath;
        currentGameImage.alt = game.title;
        currentImageContainer.classList.remove("hidden");
      } else {
        currentImageContainer.classList.add("hidden");
      }

      // Show the edit modal
      editModal.classList.remove("hidden");
    } catch (error) {
      console.error("Error loading game data:", error);
      alert("Failed to load game data. Please try again.");
    }
  }

  // Add click handlers to Edit buttons
  editButtons.forEach((button) => {
    button.addEventListener("click", (e) => {
      e.preventDefault();
      const gameId = button.getAttribute("data-id");
      if (gameId) {
        loadGameForEdit(gameId);
      }
    });
  });

  // Close edit modal handlers
  closeEditGameModal.addEventListener("click", () => {
    editModal.classList.add("hidden");
  });

  cancelEditGame.addEventListener("click", () => {
    editModal.classList.add("hidden");
  });
  // Handle edit form submission
  document
    .getElementById("editGameForm")
    .addEventListener("submit", async function (e) {
      e.preventDefault();

      // Basic validation
      const title = document.getElementById("editTitle").value.trim();
      const description = document
        .getElementById("editDescription")
        .value.trim();
      const developer = document.getElementById("editDeveloper").value.trim();
      const price = document.getElementById("editPrice").value.trim();
      const releaseDate = document
        .getElementById("editReleaseDate")
        .value.trim();

      // Get the selected platforms and genres
      const platformSelect = document.getElementById("editPlatform");
      const genreSelect = document.getElementById("editGenre");

      const selectedPlatforms = Array.from(platformSelect.selectedOptions).map(
        (option) => option.value
      );
      const selectedGenres = Array.from(genreSelect.selectedOptions).map(
        (option) => option.value
      );

      let isValid = true;
      let errorMessages = [];

      // Validate required fields
      if (!title) {
        errorMessages.push("Title is required");
        isValid = false;
      }

      if (!description) {
        errorMessages.push("Description is required");
        isValid = false;
      }

      if (!developer) {
        errorMessages.push("Developer is required");
        isValid = false;
      }

      if (!price) {
        errorMessages.push("Price is required");
        isValid = false;
      }

      if (!releaseDate) {
        errorMessages.push("Release date is required");
        isValid = false;
      }

      if (selectedPlatforms.length === 0) {
        errorMessages.push("Please select at least one platform");
        isValid = false;
      }

      if (selectedGenres.length === 0) {
        errorMessages.push("Please select at least one genre");
        isValid = false;
      }

      if (!isValid) {
        alert(
          "Please correct the following errors:\n" + errorMessages.join("\n")
        );
        return;
      }

      // Create FormData from the form
      const formData = new FormData(this);

      // Clear the existing platform and genre inputs
      formData.delete("platform");
      formData.delete("genre");

      // Add the selected platforms and genres as separate values
      selectedPlatforms.forEach((platform) => {
        formData.append("platform", platform);
      });

      selectedGenres.forEach((genre) => {
        formData.append("genre", genre);
      });

      try {
        const response = await fetch(this.action, {
          method: "POST",
          body: formData,
        });

        if (!response.ok) {
          throw new Error("Network response was not ok");
        }

        const result = await response.json();

        if (result.success) {
          // Hide the modal
          editModal.classList.add("hidden");

          // Reload the page to show updated data
          window.location.reload();
        } else {
          alert(result.message || "Failed to update game. Please try again.");
        }
      } catch (error) {
        console.error("Error updating game:", error);
        alert("An error occurred while updating the game. Please try again.");
      }
    });
  // Close modals when clicking outside
  window.addEventListener("click", (e) => {
    if (e.target === addGameModal) {
      addGameModal.classList.add("hidden");
    }
    if (e.target === deleteModal) {
      deleteModal.classList.add("hidden");
    }
    if (e.target === editGameModal) {
      editGameModal.classList.add("hidden");
    }
  });

  // Add Game Form Validation and Submission
  document
    .getElementById("addGameForm")
    .addEventListener("submit", function (e) {
      e.preventDefault();

      // Basic validation
      const title = document.getElementById("title").value.trim();
      const description = document.getElementById("description").value.trim();
      const developer = document.getElementById("developer").value.trim();
      const price = document.getElementById("price").value.trim();
      const releaseDate = document.getElementById("releaseDate").value.trim();

      // Get the selected platforms and genres
      const platformSelect = document.getElementById("platform");
      const genreSelect = document.getElementById("genre");

      const selectedPlatforms = Array.from(platformSelect.selectedOptions).map(
        (option) => option.value
      );
      const selectedGenres = Array.from(genreSelect.selectedOptions).map(
        (option) => option.value
      );

      let isValid = true;
      let errorMessages = [];

      // Validate required fields
      if (!title) {
        errorMessages.push("Title is required");
        isValid = false;
      }

      if (!description) {
        errorMessages.push("Description is required");
        isValid = false;
      }

      if (!developer) {
        errorMessages.push("Developer is required");
        isValid = false;
      }

      if (!price) {
        errorMessages.push("Price is required");
        isValid = false;
      }

      if (!releaseDate) {
        errorMessages.push("Release date is required");
        isValid = false;
      }

      if (selectedPlatforms.length === 0) {
        errorMessages.push("Please select at least one platform");
        isValid = false;
      }

      if (selectedGenres.length === 0) {
        errorMessages.push("Please select at least one genre");
        isValid = false;
      }

      if (!isValid) {
        alert(
          "Please correct the following errors:\n" + errorMessages.join("\n")
        );
        return;
      }

      // Create a FormData object
      const formData = new FormData(this);

      // Clear the existing platform and genre inputs
      formData.delete("platform");
      formData.delete("genre");

      // Add the selected platforms and genres as separate values
      selectedPlatforms.forEach((platform) => {
        formData.append("platform", platform);
      });

      selectedGenres.forEach((genre) => {
        formData.append("genre", genre);
      });

      // Submit the form using fetch
      fetch(this.action, {
        method: "POST",
        body: formData,
      })
        .then((response) => {
          if (!response.ok) {
            throw new Error("Network response was not ok");
          }
          window.location.reload();
        })
        .catch((error) => {
          console.error("Error submitting form:", error);
          alert("Failed to add game. Please try again.");
        });
    });
</script>

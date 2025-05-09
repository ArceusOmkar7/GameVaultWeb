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
      const imageUploadType = document.getElementById("imageUploadType").value;
      if (imageUploadType === "url") {
        const imageUrl = document.getElementById("imageUrl").value.trim();
        if (imageUrl) {
          addField("imagePath", imageUrl);
        }
      } else {
        const imageFile = document.getElementById("imageFile").files[0];
        if (imageFile) {
          // For file upload, we'll need to handle it differently
          // The server will need to process the file and set the path
          const formData = new FormData();
          formData.append("imageFile", imageFile);
          // Add other form fields to formData
          addField("imagePath", ""); // Empty path for now, server will set it
        }
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
      console.log("Fetching game data for ID:", gameId);
      const url = new URL(
        `${pageContext.request.contextPath}/admin/edit-game`,
        window.location.origin
      );
      url.searchParams.append("id", gameId);
      console.log("Request URL:", url.toString());

      const response = await fetch(url.toString(), {
        headers: {
          "X-Requested-With": "XMLHttpRequest",
        },
      });

      console.log("Response status:", response.status);
      const responseText = await response.text();
      console.log("Raw response:", responseText);

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      let data;
      try {
        data = JSON.parse(responseText);
      } catch (e) {
        console.error("Failed to parse JSON response:", e);
        throw new Error("Invalid JSON response from server");
      }

      console.log("Parsed response data:", data);

      if (!data.success) {
        throw new Error(data.message || "Failed to load game data");
      }

      const game = data.game;
      if (!game) {
        throw new Error("No game data received");
      }

      console.log("Loaded game data:", game);

      // Set the game ID first
      document.getElementById("editGameId").value = game.gameId;
      console.log("Set gameId to:", game.gameId);

      // Set other fields
      document.getElementById("editPrice").value = game.price || "";
      document.getElementById("editRating").value = game.rating || "";
      document.getElementById("editImagePath").value = game.imagePath || "";
      document.getElementById("editRatingValue").textContent =
        game.rating || "0";

      // Update image preview
      const currentImageContainer = document.getElementById(
        "currentImageContainer"
      );
      const currentGameImage = document.getElementById("currentGameImage");
      if (game.imagePath) {
        currentGameImage.src = game.imagePath;
        currentGameImage.alt = game.title || "";
        currentImageContainer.classList.remove("hidden");
      } else {
        currentImageContainer.classList.add("hidden");
      }

      // Show the modal
      document.getElementById("editGameModal").classList.remove("hidden");
    } catch (error) {
      console.error("Error loading game data:", error);
      alert("Error loading game data: " + error.message);
    }
  }

  // Add click handlers to Edit buttons
  editButtons.forEach((button) => {
    button.addEventListener("click", (e) => {
      e.preventDefault();
      const gameId = button.getAttribute("data-id");
      console.log("Edit button clicked, gameId:", gameId);
      if (!gameId) {
        console.error("No game ID found on edit button");
        alert("Error: No game ID found");
        return;
      }
      loadGameForEdit(gameId);
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

      // Only validate and submit the fields that exist in the modal
      const price = document.getElementById("editPrice").value.trim();
      const rating = document.getElementById("editRating").value;
      const imagePath = document.getElementById("editImagePath").value.trim();
      const gameId = document.getElementById("editGameId").value;
      console.log("Submitting form with gameId:", gameId);

      let isValid = true;
      let errorMessages = [];

      if (!gameId) {
        errorMessages.push("Game ID is missing");
        isValid = false;
      }

      if (!price || isNaN(price) || Number(price) < 0) {
        errorMessages.push("Valid price is required");
        isValid = false;
      }
      if (
        rating === undefined ||
        rating === null ||
        isNaN(rating) ||
        Number(rating) < 0 ||
        Number(rating) > 5
      ) {
        errorMessages.push("Valid rating (0-5) is required");
        isValid = false;
      }
      if (!isValid) {
        alert(
          "Please correct the following errors:\n" + errorMessages.join("\n")
        );
        return;
      }

      // Create FormData from the form
      const formData = new URLSearchParams();
      formData.append("gameId", gameId);
      formData.append("price", price);
      formData.append("rating", rating);
      formData.append("imagePath", imagePath);

      console.log("Submitting form data:", {
        gameId,
        price,
        rating,
        imagePath,
      });

      try {
        const response = await fetch(this.action, {
          method: "POST",
          body: formData,
          headers: {
            "X-Requested-With": "XMLHttpRequest",
            "Content-Type": "application/x-www-form-urlencoded",
          },
        });

        if (!response.ok) {
          throw new Error("Network response was not ok");
        }

        const result = await response.json();
        console.log("Server response:", result);

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
      const formData = new FormData();
      formData.append("title", title);
      formData.append("description", description);
      formData.append("developer", developer);
      formData.append("price", price);
      formData.append("releaseDate", releaseDate);

      // Handle image upload
      const imageUploadType = document.getElementById("imageUploadType").value;
      if (imageUploadType === "url") {
        const imageUrl = document.getElementById("imageUrl").value.trim();
        if (imageUrl) {
          formData.append("imageUrl", imageUrl);
        }
      } else {
        const imageFile = document.getElementById("imageFile").files[0];
        if (imageFile) {
          formData.append("imageFile", imageFile);
        }
      }

      // Add the rating if present
      const rating = this.querySelector("#rating").value;
      if (rating) {
        formData.append("rating", rating);
      }

      // Add each selected platform as a separate field
      selectedPlatforms.forEach((platform) => {
        formData.append("platform", platform);
      });

      // Add each selected genre as a separate field
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

  // Handle image upload type toggle
  const imageUploadType = document.getElementById("imageUploadType");
  const imageFileContainer = document.getElementById("imageFileContainer");
  const imageUrlContainer = document.getElementById("imageUrlContainer");

  if (imageUploadType) {
    imageUploadType.addEventListener("change", function () {
      if (this.value === "file") {
        imageFileContainer.classList.remove("hidden");
        imageUrlContainer.classList.add("hidden");
      } else {
        imageFileContainer.classList.add("hidden");
        imageUrlContainer.classList.remove("hidden");
      }
    });
  }
</script>

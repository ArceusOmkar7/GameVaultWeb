<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script>
  // Add Game Modal
  const addGameBtn = document.getElementById("addGameBtn");
  const addGameModal = document.getElementById("addGameModal");
  const closeAddGameModal = document.getElementById("closeAddGameModal");
  const cancelAddGame = document.getElementById("cancelAddGame");

  addGameBtn.addEventListener("click", () => {
    addGameModal.classList.remove("hidden");
  });

  closeAddGameModal.addEventListener("click", () => {
    addGameModal.classList.add("hidden");
  });

  cancelAddGame.addEventListener("click", () => {
    addGameModal.classList.add("hidden");
  });

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

      const game = await response.json();

      // Populate form fields
      document.getElementById("editGameId").value = game.gameId;
      document.getElementById("editTitle").value = game.title || "";
      document.getElementById("editDescription").value = game.description || "";
      document.getElementById("editDeveloper").value = game.developer || "";
      document.getElementById("editPrice").value = game.price || "";
      document.getElementById("editPlatform").value = game.platform || "";
      document.getElementById("editGenre").value = game.genre || "";
      document.getElementById("editRating").value = game.rating || "";
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

      // Create FormData from the form
      const formData = new FormData(this);

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
</script>

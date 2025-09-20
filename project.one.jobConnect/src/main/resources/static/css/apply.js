document.addEventListener('DOMContentLoaded', function() {
  const applyButtons = document.querySelectorAll('.applyBtn');

  applyButtons.forEach(button => {
    button.addEventListener('click', function() {
      // Get data from the button's attributes
      const jobId = this.dataset.jobId;
      const jobTitle = this.dataset.jobTitle;
      const employerEmail = this.dataset.employerEmail;
      const userEmail = this.dataset.userEmail;

      // Make an API call to your Spring controller
      fetch(`/apply/${jobId}?userEmail=${userEmail}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          jobId: jobId,
          jobTitle: jobTitle,
          employerEmail: employerEmail,
          applicantEmail: userEmail
        })
      })
      .then(response => {
        if (response.ok) {
          // Change button text and color on success
          this.textContent = 'Applied';
          this.style.backgroundColor = '#00BFFF';
          this.disabled = true; // Disable the button after a successful application
        } else {
          // Handle errors
          console.error('Failed to apply for job.');
          alert('Error: Could not apply for the job. Please try again.');
        }
      })
      .catch(error => {
        console.error('Error:', error);
        alert('An unexpected error occurred.');
      });
    });
  });
});
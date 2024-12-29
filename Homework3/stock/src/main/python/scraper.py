import requests
from bs4 import BeautifulSoup

# Step 1: Send a GET request to the website
url = 'https://www.mse.mk/en/stats/symbolhistory/mpt'  # Replace with the URL of the site you want to scrape
response = requests.get(url)

# Step 2: Parse the HTML content using BeautifulSoup
soup = BeautifulSoup(response.text, 'html.parser')

# Step 3: Extract data (example: extract all links)
options = soup.find_all('option')

issuer_names = []
# Print the URLs of all links on the page
for option in options:
    issuer_names.append(option.text)

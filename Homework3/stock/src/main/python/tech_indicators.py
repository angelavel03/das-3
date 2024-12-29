import requests
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import json
from bs4 import BeautifulSoup

# Step 1: Send a GET request to the website
url = 'https://www.mse.mk/en/stats/symbolhistory/mpt'  # Replace with the URL of the site you want to scrape
response = requests.get(url)

# Step 2: Parse the HTML content using BeautifulSoup
soup = BeautifulSoup(response.text, 'html.parser')

# Step 3: Extract data (example: extract all links)
options = soup.find_all('option')

# Load data from the API
def load_data_from_url(url):
    response = requests.get(url)
    response.raise_for_status()  # Ensure we handle HTTP errors
    return response.json()

for issuer in options:
    api_url = f"http://localhost:8080/api/stocks/" + issuer.text  # Fetch data from API URL

    # Step 2: Process the data into a DataFrame
    data_list = load_data_from_url(api_url)
    df = pd.DataFrame(data_list)
    df['date'] = pd.to_datetime(df['date'])  # Convert to datetime
    df.set_index('date', inplace=True)

    # Step 3: Calculate Moving Averages and Other Indicators
    df['SMA'] = df['lastTradePrice'].rolling(window=30).mean()  # Simple Moving Average (SMA)
    df['EMA'] = df['lastTradePrice'].ewm(span=30, adjust=False).mean()  # Exponential Moving Average (EMA)

    # Relative Strength Index (RSI)
    delta = df['lastTradePrice'].diff()
    gain = delta.where(delta > 0, 0)
    loss = -delta.where(delta < 0, 0)
    avg_gain = gain.rolling(window=14).mean()
    avg_loss = loss.rolling(window=14).mean()
    rs = avg_gain / avg_loss
    df['RSI'] = 100 - (100 / (1 + rs))

    # Moving Average Convergence Divergence (MACD)
    df['EMA12'] = df['lastTradePrice'].ewm(span=12, adjust=False).mean()
    df['EMA26'] = df['lastTradePrice'].ewm(span=26, adjust=False).mean()
    df['MACD'] = df['EMA12'] - df['EMA26']
    df['MACD_signal'] = df['MACD'].ewm(span=9, adjust=False).mean()

    # Williams %R
    highest_high = df['lastTradePrice'].rolling(window=14).max()
    lowest_low = df['lastTradePrice'].rolling(window=14).min()
    df['Williams %R'] = ((highest_high - df['lastTradePrice']) / (highest_high - lowest_low)) * -100

    # Ultimate Oscillator
    fast_period = 7
    mid_period = 14
    slow_period = 28
    df['fast'] = (df['lastTradePrice'].rolling(window=fast_period).max() - df['lastTradePrice']) / \
        (df['lastTradePrice'].rolling(window=fast_period).max() - df['lastTradePrice'].rolling(window=fast_period).min())
    df['mid'] = (df['lastTradePrice'].rolling(window=mid_period).max() - df['lastTradePrice']) / \
        (df['lastTradePrice'].rolling(window=mid_period).max() - df['lastTradePrice'].rolling(window=mid_period).min())
    df['slow'] = (df['lastTradePrice'].rolling(window=slow_period).max() - df['lastTradePrice']) / \
        (df['lastTradePrice'].rolling(window=slow_period).max() - df['lastTradePrice'].rolling(window=slow_period).min())

    df['Ultimate Oscillator'] = (4 * df['fast'] + 2 * df['mid'] + df['slow']) / 7

    # Step 4: Generate Buy/Sell/Hold signals based on indicator thresholds
    df['RSI_signal'] = np.where(df['RSI'] < 30, 'Buy', np.where(df['RSI'] > 70, 'Sell', 'Hold'))
    df['MACD_signal'] = np.where(df['MACD'] > df['MACD_signal'], 'Buy', np.where(df['MACD'] < df['MACD_signal'], 'Sell', 'Hold'))
    df['Williams_signal'] = np.where(df['Williams %R'] < -80, 'Buy', np.where(df['Williams %R'] > -20, 'Sell', 'Hold'))
    df['Ultimate_signal'] = np.where(df['Ultimate Oscillator'] < 30, 'Buy', np.where(df['Ultimate Oscillator'] > 70, 'Sell', 'Hold'))

    # Step 5: Prepare the output in JSON format for Java to consume
    output = {
        "stock": "ALK",
        "signals": {
            "RSI_signal": df['RSI_signal'].tail(1).iloc[0],
            "MACD_signal": df['MACD_signal'].tail(1).iloc[0],
            "Williams_signal": df['Williams_signal'].tail(1).iloc[0],
            "Ultimate_signal": df['Ultimate_signal'].tail(1).iloc[0]
        }
    }

    # Return the result as JSON for Java to consume
    print(json.dumps(output))

    # Optionally, save the plot
    image_path = f"./results/{issuer.text}_stock_analysis.png"
    plt.figure(figsize=(12, 6))
    plt.plot(df['lastTradePrice'], label='Last Trade Price', color='black')
    plt.plot(df['SMA'], label='SMA (30)', color='blue')
    plt.plot(df['EMA'], label='EMA (30)', color='red')
    plt.title(f"Stock Price Analysis with Technical Indicators for ALK")
    plt.xlabel('Date')
    plt.ylabel('Price')
    plt.legend()
    plt.grid(True)
    plt.savefig(image_path)
    plt.savefig(image_path)

import pandas as pd
import matplotlib.pyplot as plt
import requests
import sys
import numpy as np
import json

# Load data from a JURL
def load_data_from_url(url):
    response = requests.get(url)
    response.raise_for_status()  # Ensure we handle HTTP errors
    data = response.json()
    return pd.DataFrame(data)

name = sys.argv[1]

# Ensure the JSON file is correctly loaded
data = load_data_from_url("http://localhost:8080/api/stocks/" + name)

# Ensure the Date column is in datetime format
data['date'] = pd.to_datetime(data['date'])
data = data.sort_values(by='date')  # Sort by date

# Calculate SMA (Simple Moving Average)
data['SMA_20'] = data['lastTradePrice'].rolling(window=20).mean()
data['SMA_50'] = data['lastTradePrice'].rolling(window=50).mean()

# Calculate EMA (Exponential Moving Average)
data['EMA_20'] = data['lastTradePrice'].ewm(span=20, adjust=False).mean()
data['EMA_50'] = data['lastTradePrice'].ewm(span=50, adjust=False).mean()

# Calculate RSI (Relative Strength Index)
def calculate_rsi(prices, window=14):
    delta = prices.diff()
    gain = (delta.where(delta > 0, 0)).rolling(window=window).mean()
    loss = (-delta.where(delta < 0, 0)).rolling(window=window).mean()
    rs = gain / loss
    rsi = 100 - (100 / (1 + rs))
    return rsi

data['RSI'] = calculate_rsi(data['lastTradePrice'])

# Plot the data
plt.figure(figsize=(14, 8))

# Plot Last trade price and moving averages
plt.subplot(2, 1, 1)
plt.plot(data['date'], data['lastTradePrice'], label='Last Trade Price', color='black')
plt.plot(data['date'], data['SMA_20'], label='SMA 20', color='blue', linestyle='--')
plt.plot(data['date'], data['SMA_50'], label='SMA 50', color='red', linestyle='--')
plt.plot(data['date'], data['EMA_20'], label='EMA 20', color='green', linestyle='-.')
plt.plot(data['date'], data['EMA_50'], label='EMA 50', color='orange', linestyle='-.')
plt.title('Stock Prices with Moving Averages')
plt.legend(loc='upper left')
plt.grid()

# Plot RSI
plt.subplot(2, 1, 2)
plt.plot(data['date'], data['RSI'], label='RSI', color='purple')
plt.axhline(70, color='red', linestyle='--', linewidth=0.8, label='Overbought (70)')
plt.axhline(30, color='green', linestyle='--', linewidth=0.8, label='Oversold (30)')
plt.title('Relative Strength Index (RSI)')
plt.legend(loc='upper left')
plt.grid()

plt.tight_layout()
plt.savefig(f"/src/main/python/analysis/{name}.png", dpi=300)
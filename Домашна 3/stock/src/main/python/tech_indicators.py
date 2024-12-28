import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import json

# Load data from a JSON file
def load_data_from_json(file_path):
    with open(file_path, 'r') as file:
        data = json.load(file)
    return pd.DataFrame(data)

# Ensure the JSON file is correctly loaded
data = load_data_from_json("stock_data.json")

# Ensure the Date column is in datetime format
data['Date'] = pd.to_datetime(data['Date'])
data = data.sort_values(by='Date')  # Sort by date

# Calculate SMA (Simple Moving Average)
data['SMA_20'] = data['Last trade price'].rolling(window=20).mean()
data['SMA_50'] = data['Last trade price'].rolling(window=50).mean()

# Calculate EMA (Exponential Moving Average)
data['EMA_20'] = data['Last trade price'].ewm(span=20, adjust=False).mean()
data['EMA_50'] = data['Last trade price'].ewm(span=50, adjust=False).mean()

# Calculate RSI (Relative Strength Index)
def calculate_rsi(prices, window=14):
    delta = prices.diff()
    gain = (delta.where(delta > 0, 0)).rolling(window=window).mean()
    loss = (-delta.where(delta < 0, 0)).rolling(window=window).mean()
    rs = gain / loss
    rsi = 100 - (100 / (1 + rs))
    return rsi

data['RSI'] = calculate_rsi(data['Last trade price'])

# Plot the data
plt.figure(figsize=(14, 8))

# Plot Last trade price and moving averages
plt.subplot(2, 1, 1)
plt.plot(data['Date'], data['Last trade price'], label='Last Trade Price', color='black')
plt.plot(data['Date'], data['SMA_20'], label='SMA 20', color='blue', linestyle='--')
plt.plot(data['Date'], data['SMA_50'], label='SMA 50', color='red', linestyle='--')
plt.plot(data['Date'], data['EMA_20'], label='EMA 20', color='green', linestyle='-.')
plt.plot(data['Date'], data['EMA_50'], label='EMA 50', color='orange', linestyle='-.')
plt.title('Stock Prices with Moving Averages')
plt.legend(loc='upper left')
plt.grid()

# Plot RSI
plt.subplot(2, 1, 2)
plt.plot(data['Date'], data['RSI'], label='RSI', color='purple')
plt.axhline(70, color='red', linestyle='--', linewidth=0.8, label='Overbought (70)')
plt.axhline(30, color='green', linestyle='--', linewidth=0.8, label='Oversold (30)')
plt.title('Relative Strength Index (RSI)')
plt.legend(loc='upper left')
plt.grid()

plt.tight_layout()
plt.savefig("stock_analysis_plot.png", dpi=300)
plt.show()
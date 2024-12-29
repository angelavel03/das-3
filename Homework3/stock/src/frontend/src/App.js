import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from "./Home.js";
import List from './List.js';
//import List from "./List.js";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/stock/:name" element={<List />} />
      </Routes>
    </Router>
  )
}

export default App;

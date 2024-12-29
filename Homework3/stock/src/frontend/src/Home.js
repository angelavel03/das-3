import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import './App.css';

export default function Home() {
    const [state, setState] = useState([]);
    const [selectedCompany, setSelectedCompany] = useState("");

    // Fetch the list of companies
    useEffect(() => {
        fetch("http://localhost:8080/")
            .then(response => {
                if (!response.ok) {
                    throw new Error("Failed to fetch companies");
                }
                return response.json();
            })
            .then(data => setState(data))
            .catch(error => console.error("Error fetching companies:", error));
    }, []);

    return (
        <>
            <h1 id="homepage-title">Прегледај ги акциите на твојата омилена компанија</h1>
            <p id="kompanija">Одбери компанија</p>
            <form id="form">
                <select
                    name="companies"
                    id="comp"
                    onChange={(e) => setSelectedCompany(e.target.value)}
                >
                    <option value="">--Избери компанија--</option>
                    {state.map((issuer) => (
                        <option key={issuer} value={issuer}>
                            {issuer}
                        </option>
                    ))}
                </select>
                <Link to={`/stock/${selectedCompany}`}>
                    <button id="btn-odberi" type="button" disabled={!selectedCompany}>
                        Одбери
                    </button>
                </Link>
            </form>
        </>
    );
}
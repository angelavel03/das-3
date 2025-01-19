import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import './App.css';

const List = () => {
    const [state, setState] = useState([]);
    const [fetched, setFetched] = useState(false);
    const { name } = useParams();
    const navigate = useNavigate();

    // Fetch the stock data for the selected company
    useEffect(() => {
        fetch(`http://localhost:8080/api/stocks/${name}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error("Во моментов нема податоци за " + name);
                }
                setFetched(true);
                return response.json();
            })
            .then(data => setState(data))
            .catch(error => {
                alert(error);
            });
    }, [name, navigate]);

    return (
        <>
            <h1 id="stock-title">Податоци за {name}</h1>
            <div id="table-wrapper">
                <button id="nazad" onClick={() => navigate("/")}>
                    Назад кон почетна страница
                </button>
                <p id="podatoci">Податоци</p>
                <table id="tabela">
                    <thead>
                        <tr>
                            <th>Датум</th>
                            <th>Цена на последна трансакција</th>
                            <th>Макс.</th>
                            <th>Мин.</th>
                            <th>Просечна цена</th>
                            <th>%пром</th>
                            <th>Количина</th>
                            <th>Промет во БЕСТ (денари)</th>
                            <th>Вкупен промет во денари</th>
                        </tr>
                    </thead>
                    <tbody>
                        {state.map(stock => (
                            <tr key={stock.id}>
                                <td>{stock.date}</td>
                                <td>{stock.lastTradePrice}</td>
                                <td>{stock.max}</td>
                                <td>{stock.min}</td>
                                <td>{stock.avgPrice}</td>
                                <td>{stock.cfg}</td>
                                <td>{stock.volume}</td>
                                <td>{stock.turnoverBestDenars}</td>
                                <td>{stock.totalTurnoverDenars}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
                {!fetched && <h4>Во моментов нема достапни податоци за {name}</h4>}
            </div>
        </>
    );
};

export default List;

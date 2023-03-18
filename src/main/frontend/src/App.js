import logo from './logo.svg';
import './App.css';
import { useState, useEffect } from 'react'

const server = "http://localhost:8080";

function FilmList() {
  const [films, setFilms] = useState([]);
  
  useEffect(() => {
    fetch(server + "/api/v1/film")
      .then((response) => response.json())
      .then((response) => {
        setFilms(response);
      })
      .catch(err =>  console.error(err.msg))
    }, []);
  
  return(
      <div className="ListOfFilms">
          <ul>
          {films.map(({id, title}) => (
              <li key={id}>{title}</li>
              ))}
          </ul>
      </div>
  );
}
function App() {
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <FilmList>
        </FilmList>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
    </div>
  );
}

export default App;

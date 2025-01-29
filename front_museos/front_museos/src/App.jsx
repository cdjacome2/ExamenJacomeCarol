import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import { Layout } from "antd";
import Navbar from "./components/Navbar";
import MuseosPage from "./pages/MuseosPage";
import CuradoresPage from "./pages/CuradoresPage";

const { Content } = Layout;

function App() {
  return (
    <Router>
      <Layout>
        <Navbar />
        <Content style={{ padding: "20px" }}>
          <Routes>
            <Route path="/" element={<MuseosPage />} />
            <Route path="/curadores" element={<CuradoresPage />} />
          </Routes>
        </Content>
      </Layout>
    </Router>
  );
}

export default App;

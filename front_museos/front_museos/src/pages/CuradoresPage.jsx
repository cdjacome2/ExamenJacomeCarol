import { useEffect, useState } from "react";
import { fetchCuradores } from "../services/api";
import { Table, Card, Spin } from "antd";

const CuradoresPage = () => {
  const [curadores, setCuradores] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchCuradores().then((data) => {
      setCuradores(data);
      setLoading(false);
    });
  }, []);

  const columns = [
    { title: "ID", dataIndex: "id", key: "id" },
    { title: "Nombre", dataIndex: "nombre", key: "nombre" },
    { title: "Especialidad", dataIndex: "especialidad", key: "especialidad" },
  ];

  return (
    <Card title="Lista de Curadores" style={{ margin: 20 }}>
      {loading ? <Spin size="large" /> : <Table dataSource={curadores} columns={columns} rowKey="id" />}
    </Card>
  );
};

export default CuradoresPage;

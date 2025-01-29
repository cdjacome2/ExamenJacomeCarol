import React, { useEffect, useState } from "react";
import { fetchMuseos, addMuseo, updateMuseo, deleteMuseo } from "../services/api";
import { Table, Button, Spin, Modal, Form, Input, Popconfirm } from "antd";

const MuseosPage = () => {
  const [museos, setMuseos] = useState([]);
  const [loading, setLoading] = useState(true);
  
  // 🔹 Estado para el modal de agregar
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [form] = Form.useForm();

  // 🔹 Estado para el modal de edición
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [editingMuseo, setEditingMuseo] = useState(null);

  // 🔹 Estado para el modal de detalles
  const [isDetailModalOpen, setIsDetailModalOpen] = useState(false);
  const [selectedMuseo, setSelectedMuseo] = useState(null);

  // Cargar museos al inicio
  useEffect(() => {
    const getMuseos = async () => {
      try {
        const data = await fetchMuseos();
        setMuseos(data);
      } catch (error) {
        console.error("Error al obtener museos:", error);
      } finally {
        setLoading(false);
      }
    };
    getMuseos();
  }, []);

  // 🔹 Mostrar el modal de detalles
  const showDetailModal = (museo) => {
    setSelectedMuseo(museo);
    setIsDetailModalOpen(true);
  };

  // 🔹 Mostrar el modal de edición y cargar los datos en el formulario
  const showEditModal = (museo) => {
    setEditingMuseo(museo);
    form.setFieldsValue(museo);
    setIsEditModalOpen(true);
  };

  // 🔹 Mostrar el modal de agregar
  const showModal = () => {
    setIsModalOpen(true);
    form.resetFields();
  };

  // 🔹 Cerrar todos los modales
  const handleCancel = () => {
    setIsModalOpen(false);
    setIsEditModalOpen(false);
    setIsDetailModalOpen(false);
    form.resetFields();
  };

  // 🔹 Agregar un nuevo museo
  const handleAddMuseo = async (values) => {
    try {
      const newMuseo = await addMuseo(values);
      setMuseos([...museos, newMuseo]);
      setIsModalOpen(false);
      form.resetFields();
    } catch (error) {
      console.error("Error al agregar museo:", error);
    }
  };

  // 🔹 Editar un museo existente
  const handleEditMuseo = async (values) => {
    try {
      const updatedMuseo = await updateMuseo(editingMuseo.id, values);
      setMuseos(museos.map((m) => (m.id === updatedMuseo.id ? updatedMuseo : m)));
      setIsEditModalOpen(false);
      form.resetFields();
    } catch (error) {
      console.error("Error al editar museo:", error);
    }
  };

  // 🔹 Eliminar un museo
  const handleDeleteMuseo = async (id) => {
    try {
      await deleteMuseo(id);
      setMuseos(museos.filter((m) => m.id !== id));
    } catch (error) {
      console.error("Error al eliminar museo:", error);
    }
  };

  // 🔹 Columnas de la tabla
  const columns = [
    { title: "ID", dataIndex: "id", key: "id" },
    { title: "Nombre", dataIndex: "nombre", key: "nombre" },
    { title: "Ubicación", dataIndex: "ubicacion", key: "ubicacion" },
    {
      title: "Acciones",
      key: "acciones",
      render: (_, record) => (
        <>
          <Button onClick={() => showDetailModal(record)} style={{ marginRight: 8 }}>🔍 Ver</Button>
          <Button onClick={() => showEditModal(record)} style={{ marginRight: 8 }}>✏️ Editar</Button>
          <Popconfirm
            title="¿Seguro que deseas eliminar este museo?"
            onConfirm={() => handleDeleteMuseo(record.id)}
            okText="Sí"
            cancelText="No"
          >
            <Button danger>🗑️ Eliminar</Button>
          </Popconfirm>
        </>
      ),
    }
  ];

  return (
    <div>
      <h2>Lista de Museos</h2>
      <Button type="primary" style={{ marginBottom: 16 }} onClick={showModal}>
        Agregar Museo
      </Button>
      
      {loading ? <Spin size="large" /> : <Table dataSource={museos} columns={columns} rowKey="id" pagination={{ position: ['bottomCenter'] }} />}

      {/* 🏛️ Modal para agregar un nuevo museo */}
      <Modal
        title="Agregar Museo"
        open={isModalOpen}
        onCancel={handleCancel}
        onOk={() => form.submit()}
      >
        <Form form={form} layout="vertical" onFinish={handleAddMuseo}>
          <Form.Item name="nombre" label="Nombre" rules={[{ required: true, message: "El nombre es obligatorio" }]}>
            <Input />
          </Form.Item>
          <Form.Item name="ubicacion" label="Ubicación" rules={[{ required: true, message: "La ubicación es obligatoria" }]}>
            <Input />
          </Form.Item>
        </Form>
      </Modal>

      {/* 🛠️ Modal para editar museo */}
      <Modal
        title="Editar Museo"
        open={isEditModalOpen}
        onCancel={handleCancel}
        onOk={() => form.submit()}
      >
        <Form form={form} layout="vertical" onFinish={handleEditMuseo}>
          <Form.Item name="nombre" label="Nombre" rules={[{ required: true, message: "El nombre es obligatorio" }]}>
            <Input />
          </Form.Item>
          <Form.Item name="ubicacion" label="Ubicación" rules={[{ required: true, message: "La ubicación es obligatoria" }]}>
            <Input />
          </Form.Item>
        </Form>
      </Modal>

      {/* 🔍 Modal para ver detalles */}
      <Modal
        title="Detalles del Museo"
        open={isDetailModalOpen}
        onCancel={handleCancel}
        footer={null} // 🔹 Sin botones en detalles
      >
        {selectedMuseo && (
          <div>
            <p><strong>ID:</strong> {selectedMuseo.id}</p>
            <p><strong>Nombre:</strong> {selectedMuseo.nombre}</p>
            <p><strong>Ubicación:</strong> {selectedMuseo.ubicacion}</p>
          </div>
        )}
      </Modal>

    </div>
  );
};

export default MuseosPage;

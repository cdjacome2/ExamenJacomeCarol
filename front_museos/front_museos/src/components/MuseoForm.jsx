import { Modal, Form, Input } from "antd";
import { useEffect } from "react";

const MuseoForm = ({ visible, onCancel, onSubmit, initialValues }) => {
  const [form] = Form.useForm(); // Asegura que la instancia de `form` esté bien conectada

  useEffect(() => {
    form.setFieldsValue(initialValues || {});
  }, [initialValues, form]);

  return (
    <Modal
        title={initialValues?.id ? "Editar Museo" : "Agregar Museo"}
        open={visible}
        onCancel={onCancel}
        onOk={() => form.submit()}
        destroyOnClose={true} // 🔹 Esto limpia el contenido al cerrar el modal
    >

      <Form form={form} layout="vertical" onFinish={onSubmit}>
        <Form.Item name="nombre" label="Nombre" rules={[{ required: true, message: "El nombre es obligatorio" }]}>
          <Input />
        </Form.Item>
        <Form.Item name="ubicacion" label="Ubicación" rules={[{ required: true, message: "La ubicación es obligatoria" }]}>
          <Input />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default MuseoForm;

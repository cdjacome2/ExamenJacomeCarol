import axios from "axios";

// Base URLs de los microservicios
const API_MUSEOS_URL = "http://localhost:8005/api/museos"; // Microservicio de museos
const API_CURADORES_URL = "http://localhost:8004/api/curadores"; // Microservicio de curadores

// Instancias de Axios para cada microservicio
const museosApi = axios.create({
  baseURL: API_MUSEOS_URL,
  headers: { "Content-Type": "application/json" },
});

const curadoresApi = axios.create({
  baseURL: API_CURADORES_URL,
  headers: { "Content-Type": "application/json" },
});

// 📌 Función para obtener la lista de museos
export const fetchMuseos = async () => {
  try {
    const response = await axios.get(API_MUSEOS_URL); // Llamado directo a la API
    console.log("Museos recibidos:", response.data); // Depuración
    return response.data;
  } catch (error) {
    console.error("Error al obtener museos:", error);
    throw error;
  }
};

export const addMuseo = async (museo) => {
    try {
      console.log("Enviando museo a:", API_MUSEOS_URL); // Verifica en la consola
  
      const response = await axios.post(API_MUSEOS_URL, museo, {
        headers: { "Content-Type": "application/json" }
      });
  
      console.log("✅ Museo agregado:", response.data);
      return response.data;
    } catch (error) {
      console.error("❌ Error al agregar museo:", error.response?.data || error.message);
      throw error;
    }
  };
  
// 📌 Función para actualizar un museo
export const updateMuseo = async (id, museo) => {
    try {
      const response = await axios.put(`${API_MUSEOS_URL}/${id}`, museo, {
        headers: { "Content-Type": "application/json" },
      });
      console.log("✅ Museo actualizado:", response.data);
      return response.data;
    } catch (error) {
      console.error("❌ Error al actualizar museo:", error.response?.data || error.message);
      throw error;
    }
  };
  
  // 📌 Función para eliminar un museo
  export const deleteMuseo = async (id) => {
    try {
      await axios.delete(`${API_MUSEOS_URL}/${id}`);
      console.log(`✅ Museo con ID ${id} eliminado`);
    } catch (error) {
      console.error("❌ Error al eliminar museo:", error.response?.data || error.message);
      throw error;
    }
  };
  
// 📌 Función para obtener la lista de curadores
export const fetchCuradores = async () => {
  try {
    const response = await axios.get(API_CURADORES_URL);
    console.log("Curadores recibidos:", response.data); // Depuración
    return response.data;
  } catch (error) {
    console.error("Error al obtener curadores:", error);
    throw error;
  }
};

// 📌 Función para agregar un curador
export const addCurador = async (curador) => {
  try {
    const response = await curadoresApi.post("/", curador);
    return response.data;
  } catch (error) {
    console.error("Error al agregar curador:", error);
    throw error;
  }
};

// 📌 Función para eliminar un curador
export const deleteCurador = async (id) => {
  try {
    await curadoresApi.delete(`/${id}`);
  } catch (error) {
    console.error("Error al eliminar curador:", error);
    throw error;
  }
};

// 📌 Función para obtener museos donde trabaja un curador
export const fetchMuseosByCurador = async (curadorId) => {
  try {
    const response = await axios.get(`${API_MUSEOS_URL}/curadores/${curadorId}/museos`);
    console.log(`Museos del curador ${curadorId}:`, response.data);
    return response.data;
  } catch (error) {
    console.error("Error al obtener museos del curador:", error);
    throw error;
  }
};

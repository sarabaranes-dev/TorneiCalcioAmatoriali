import axios from 'axios';
import type { Partita } from '../types';

const THYMELEAF_URL = 'http://localhost:8080';

export const partiteService = {
  /**
   * Recupera tutte le partite dal backend
   */
  async getPartite(): Promise<Partita[]> {
    try {
      const response = await axios.get(`${THYMELEAF_URL}/rest/partite`);
      return response.data;
    } catch (error) {
      console.error('Errore nel recupero delle partite:', error);
      throw error;
    }
  },

  /**
   * Recupera una partita per ID
   */
  async getPartitaById(id: number): Promise<Partita> {
    try {
      const response = await axios.get(`${THYMELEAF_URL}/rest/partite/${id}`);
      return response.data;
    } catch (error) {
      console.error(`Errore nel recupero della partita ${id}:`, error);
      throw error;
    }
  },
};

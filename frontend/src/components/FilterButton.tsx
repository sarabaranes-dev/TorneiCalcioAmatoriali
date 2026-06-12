import React from 'react';
import type { Partita } from '../types';

interface FilterButtonProps {
  label: string;
  stato: string | 'ALL';
  filterValue: string | 'ALL';
  isActive: boolean;
  count: number;
  onClick: () => void;
}

/**
 * Componente riutilizzabile per i bottoni di filtro
 * Riceve le props e disegna un bottone intelligente
 */
export const FilterButton: React.FC<FilterButtonProps> = ({
  label,
  stato,
  filterValue,
  isActive,
  count,
  onClick,
}) => {
  return (
    <button
      onClick={onClick}
      className={`btn-filtro ${isActive ? stato.toLowerCase() : ''}`}
    >
      {label} ({count})
    </button>
  );
};

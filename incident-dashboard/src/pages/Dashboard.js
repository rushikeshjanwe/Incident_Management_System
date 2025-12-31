import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import {
  getIncidents,
  createIncident,
  acknowledgeIncident,
  resolveIncident,
  closeIncident
} from '../services/api';

function Dashboard() {
  const [incidents, setIncidents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [newIncident, setNewIncident] = useState({ title: '', description: '', severity: 'P1' });
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    fetchIncidents();
  }, []);

  const fetchIncidents = async () => {
    try {
      const response = await getIncidents();
      setIncidents(response.data.data.content || []);
    } catch (err) {
      console.error('Failed to fetch incidents', err);
    } finally {
      setLoading(false);
    }
  };

  const handleCreate = async (e) => {
    e.preventDefault();
    try {
      await createIncident(newIncident);
      setShowModal(false);
      setNewIncident({ title: '', description: '', severity: 'P1' });
      fetchIncidents();
    } catch (err) {
      console.error('Failed to create incident', err);
    }
  };

  const handleAcknowledge = async (id) => {
    try {
      await acknowledgeIncident(id);
      fetchIncidents();
    } catch (err) {
      console.error('Failed to acknowledge', err);
    }
  };

  const handleResolve = async (id) => {
    const resolution = prompt('Enter resolution:');
    if (resolution) {
      try {
        await resolveIncident(id, resolution);
        fetchIncidents();
      } catch (err) {
        console.error('Failed to resolve', err);
      }
    }
  };

  const handleClose = async (id) => {
    try {
      await closeIncident(id);
      fetchIncidents();
    } catch (err) {
      console.error('Failed to close', err);
    }
  };

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const getStatusColor = (status) => {
    const colors = {
      TRIGGERED: '#ff6b6b',
      ACKNOWLEDGED: '#feca57',
      INVESTIGATING: '#48dbfb',
      ESCALATED: '#ff9ff3',
      RESOLVED: '#1dd1a1',
      CLOSED: '#576574',
    };
    return colors[status] || '#576574';
  };

  const getSeverityColor = (severity) => {
    const colors = {
      P1: '#ff6b6b',
      P2: '#feca57',
      P3: '#48dbfb',
      P4: '#1dd1a1',
    };
    return colors[severity] || '#576574';
  };

  const stats = {
    total: incidents.length,
    active: incidents.filter(i => !['RESOLVED', 'CLOSED'].includes(i.status)).length,
    p1: incidents.filter(i => i.severity === 'P1').length,
    p2: incidents.filter(i => i.severity === 'P2').length,
  };

  if (loading) {
    return <div style={styles.loading}>Loading...</div>;
  }

  return (
    <div style={styles.container}>
      {/* Header */}
      <header style={styles.header}>
        <h1 style={styles.logo}>🚨 Incident Management</h1>
        <div style={styles.userInfo}>
          <span style={styles.username}>👤 {user?.username}</span>
          <button onClick={handleLogout} style={styles.logoutBtn}>Logout</button>
        </div>
      </header>

      {/* Stats */}
      <div style={styles.statsContainer}>
        <div style={{ ...styles.statCard, borderLeft: '4px solid #ff6b6b' }}>
          <div style={styles.statNumber}>{stats.active}</div>
          <div style={styles.statLabel}>Active</div>
        </div>
        <div style={{ ...styles.statCard, borderLeft: '4px solid #ff6b6b' }}>
          <div style={styles.statNumber}>{stats.p1}</div>
          <div style={styles.statLabel}>P1 Critical</div>
        </div>
        <div style={{ ...styles.statCard, borderLeft: '4px solid #feca57' }}>
          <div style={styles.statNumber}>{stats.p2}</div>
          <div style={styles.statLabel}>P2 High</div>
        </div>
        <div style={{ ...styles.statCard, borderLeft: '4px solid #48dbfb' }}>
          <div style={styles.statNumber}>{stats.total}</div>
          <div style={styles.statLabel}>Total</div>
        </div>
      </div>

      {/* Create Button */}
      <button onClick={() => setShowModal(true)} style={styles.createBtn}>
        + Create New Incident
      </button>

      {/* Incidents List */}
      <div style={styles.incidentsList}>
        {incidents.length === 0 ? (
          <div style={styles.noIncidents}>No incidents found</div>
        ) : (
          incidents.map((incident) => (
            <div key={incident.id} style={styles.incidentCard}>
              <div style={styles.incidentHeader}>
                <span style={styles.incidentNumber}>{incident.incidentNumber}</span>
                <span style={{ ...styles.severity, backgroundColor: getSeverityColor(incident.severity) }}>
                  {incident.severity}
                </span>
                <span style={{ ...styles.status, backgroundColor: getStatusColor(incident.status) }}>
                  {incident.status}
                </span>
              </div>
              <h3 style={styles.incidentTitle}>{incident.title}</h3>
              <p style={styles.incidentDesc}>{incident.description || 'No description'}</p>
              <div style={styles.incidentActions}>
                {incident.status === 'TRIGGERED' && (
                  <button onClick={() => handleAcknowledge(incident.id)} style={styles.ackBtn}>
                    Acknowledge
                  </button>
                )}
                {incident.status === 'ACKNOWLEDGED' && (
                  <button onClick={() => handleResolve(incident.id)} style={styles.resolveBtn}>
                    Resolve
                  </button>
                )}
                {incident.status === 'RESOLVED' && (
                  <button onClick={() => handleClose(incident.id)} style={styles.closeBtn}>
                    Close
                  </button>
                )}
              </div>
            </div>
          ))
        )}
      </div>

      {/* Create Modal */}
      {showModal && (
        <div style={styles.modalOverlay}>
          <div style={styles.modal}>
            <h2 style={styles.modalTitle}>Create New Incident</h2>
            <form onSubmit={handleCreate}>
              <div style={styles.inputGroup}>
                <label style={styles.label}>Title</label>
                <input
                  type="text"
                  value={newIncident.title}
                  onChange={(e) => setNewIncident({ ...newIncident, title: e.target.value })}
                  style={styles.input}
                  required
                />
              </div>
              <div style={styles.inputGroup}>
                <label style={styles.label}>Description</label>
                <textarea
                  value={newIncident.description}
                  onChange={(e) => setNewIncident({ ...newIncident, description: e.target.value })}
                  style={{ ...styles.input, minHeight: '100px' }}
                />
              </div>
              <div style={styles.inputGroup}>
                <label style={styles.label}>Severity</label>
                <select
                  value={newIncident.severity}
                  onChange={(e) => setNewIncident({ ...newIncident, severity: e.target.value })}
                  style={styles.input}
                >
                  <option value="P1">P1 - Critical</option>
                  <option value="P2">P2 - High</option>
                  <option value="P3">P3 - Medium</option>
                  <option value="P4">P4 - Low</option>
                </select>
              </div>
              <div style={styles.modalActions}>
                <button type="button" onClick={() => setShowModal(false)} style={styles.cancelBtn}>
                  Cancel
                </button>
                <button type="submit" style={styles.submitBtn}>
                  Create
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

const styles = {
  container: {
    minHeight: '100vh',
    backgroundColor: '#1a1a2e',
    padding: '20px',
  },
  header: {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: '30px',
  },
  logo: {
    color: '#e94560',
    fontSize: '24px',
    margin: 0,
  },
  userInfo: {
    display: 'flex',
    alignItems: 'center',
    gap: '15px',
  },
  username: {
    color: '#ffffff',
  },
  logoutBtn: {
    padding: '8px 16px',
    backgroundColor: '#e94560',
    color: '#ffffff',
    border: 'none',
    borderRadius: '5px',
    cursor: 'pointer',
  },
  statsContainer: {
    display: 'grid',
    gridTemplateColumns: 'repeat(auto-fit, minmax(150px, 1fr))',
    gap: '20px',
    marginBottom: '30px',
  },
  statCard: {
    backgroundColor: '#16213e',
    padding: '20px',
    borderRadius: '10px',
  },
  statNumber: {
    color: '#ffffff',
    fontSize: '32px',
    fontWeight: 'bold',
  },
  statLabel: {
    color: '#a0a0a0',
    fontSize: '14px',
  },
  createBtn: {
    padding: '12px 24px',
    backgroundColor: '#e94560',
    color: '#ffffff',
    border: 'none',
    borderRadius: '5px',
    fontSize: '16px',
    cursor: 'pointer',
    marginBottom: '20px',
  },
  incidentsList: {
    display: 'flex',
    flexDirection: 'column',
    gap: '15px',
  },
  incidentCard: {
    backgroundColor: '#16213e',
    padding: '20px',
    borderRadius: '10px',
  },
  incidentHeader: {
    display: 'flex',
    alignItems: 'center',
    gap: '10px',
    marginBottom: '10px',
  },
  incidentNumber: {
    color: '#a0a0a0',
    fontSize: '14px',
  },
  severity: {
    padding: '4px 8px',
    borderRadius: '4px',
    color: '#ffffff',
    fontSize: '12px',
    fontWeight: 'bold',
  },
  status: {
    padding: '4px 8px',
    borderRadius: '4px',
    color: '#ffffff',
    fontSize: '12px',
  },
  incidentTitle: {
    color: '#ffffff',
    margin: '0 0 10px 0',
    fontSize: '18px',
  },
  incidentDesc: {
    color: '#a0a0a0',
    margin: '0 0 15px 0',
    fontSize: '14px',
  },
  incidentActions: {
    display: 'flex',
    gap: '10px',
  },
  ackBtn: {
    padding: '8px 16px',
    backgroundColor: '#feca57',
    color: '#000000',
    border: 'none',
    borderRadius: '5px',
    cursor: 'pointer',
  },
  resolveBtn: {
    padding: '8px 16px',
    backgroundColor: '#1dd1a1',
    color: '#ffffff',
    border: 'none',
    borderRadius: '5px',
    cursor: 'pointer',
  },
  closeBtn: {
    padding: '8px 16px',
    backgroundColor: '#576574',
    color: '#ffffff',
    border: 'none',
    borderRadius: '5px',
    cursor: 'pointer',
  },
  loading: {
    color: '#ffffff',
    textAlign: 'center',
    paddingTop: '100px',
    fontSize: '18px',
  },
  noIncidents: {
    color: '#a0a0a0',
    textAlign: 'center',
    padding: '40px',
    fontSize: '16px',
  },
  modalOverlay: {
    position: 'fixed',
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    backgroundColor: 'rgba(0, 0, 0, 0.7)',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
  },
  modal: {
    backgroundColor: '#16213e',
    padding: '30px',
    borderRadius: '10px',
    width: '100%',
    maxWidth: '500px',
  },
  modalTitle: {
    color: '#ffffff',
    marginBottom: '20px',
  },
  inputGroup: {
    marginBottom: '20px',
  },
  label: {
    display: 'block',
    color: '#ffffff',
    marginBottom: '8px',
  },
  input: {
    width: '100%',
    padding: '12px',
    borderRadius: '5px',
    border: '1px solid #0f3460',
    backgroundColor: '#0f3460',
    color: '#ffffff',
    fontSize: '14px',
    boxSizing: 'border-box',
  },
  modalActions: {
    display: 'flex',
    gap: '10px',
    justifyContent: 'flex-end',
  },
  cancelBtn: {
    padding: '10px 20px',
    backgroundColor: '#576574',
    color: '#ffffff',
    border: 'none',
    borderRadius: '5px',
    cursor: 'pointer',
  },
  submitBtn: {
    padding: '10px 20px',
    backgroundColor: '#e94560',
    color: '#ffffff',
    border: 'none',
    borderRadius: '5px',
    cursor: 'pointer',
  },
};

export default Dashboard;
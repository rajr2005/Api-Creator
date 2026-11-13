import { useEffect, useState } from "react";

const API_BASE_URL = (import.meta.env.VITE_API_URL || "").replace(/\/$/, "");

const emptyForm = {
  name: "",
  method: "GET",
  path: "/users",
  statusCode: 200,
  contentType: "application/json",
  responseBody: '{\n  "message": "Hello from MockForge"\n}',
  headersText: "X-Mock-Source: MockForge"
};

function App() {
  const [endpoints, setEndpoints] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);
  const [testingId, setTestingId] = useState("");
  const [testResult, setTestResult] = useState("");

  useEffect(() => {
    loadEndpoints();
  }, []);

  async function loadEndpoints() {
    setLoading(true);
    setError("");
    try {
      const response = await fetch(`${API_BASE_URL}/api/mock-endpoints`);
      if (!response.ok) {
        throw new Error("Unable to load mock endpoints.");
      }
      const data = await response.json();
      setEndpoints(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  function updateField(event) {
    const { name, value } = event.target;
    setForm((current) => ({
      ...current,
      [name]: name === "statusCode" ? Number(value) : value
    }));
  }

  function parseHeaders(headersText) {
    return headersText
      .split("\n")
      .map((line) => line.trim())
      .filter(Boolean)
      .reduce((acc, line) => {
        const separatorIndex = line.indexOf(":");
        if (separatorIndex === -1) {
          return acc;
        }
        const key = line.slice(0, separatorIndex).trim();
        const value = line.slice(separatorIndex + 1).trim();
        if (key) {
          acc[key] = value;
        }
        return acc;
      }, {});
  }

  async function createEndpoint(event) {
    event.preventDefault();
    setError("");

    try {
      const response = await fetch(`${API_BASE_URL}/api/mock-endpoints`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          name: form.name,
          method: form.method,
          path: form.path,
          statusCode: form.statusCode,
          contentType: form.contentType,
          responseBody: form.responseBody,
          headers: parseHeaders(form.headersText)
        })
      });

      if (!response.ok) {
        const message = await response.text();
        throw new Error(message || "Unable to create mock endpoint.");
      }

      setForm(emptyForm);
      setTestResult("");
      await loadEndpoints();
    } catch (err) {
      setError(err.message);
    }
  }

  async function deleteEndpoint(id) {
    setError("");
    try {
      const response = await fetch(`${API_BASE_URL}/api/mock-endpoints/${id}`, {
        method: "DELETE"
      });
      if (!response.ok) {
        throw new Error("Unable to delete mock endpoint.");
      }
      if (testingId === id) {
        setTestingId("");
        setTestResult("");
      }
      await loadEndpoints();
    } catch (err) {
      setError(err.message);
    }
  }

  async function testEndpoint(endpoint) {
    setTestingId(endpoint.id);
    setTestResult("Loading mock response...");
    setError("");

    try {
      const response = await fetch(`${API_BASE_URL}/mock${endpoint.path}`, {
        method: endpoint.method
      });
      const body = await response.text();
      setTestResult(`Status: ${response.status}\n\n${body}`);
    } catch (err) {
      setError(err.message);
      setTestResult("");
    }
  }

  return (
    <div className="page-shell">
      <section className="hero">
        <div>
          <p className="eyebrow">Spring Boot + React</p>
          <h1>Build and publish your own mock APIs in minutes.</h1>
          <p className="hero-copy">
            Create an endpoint, choose the HTTP method and response, then test it instantly through a generated mock URL.
          </p>
        </div>
        <div className="hero-card">
          <span>Mock URL pattern</span>
          <code>http://localhost:8080/mock/your-path</code>
          <p>Example: GET /mock/users</p>
        </div>
      </section>

      <main className="grid">
        <section className="panel">
          <div className="panel-heading">
            <h2>Create a mock API</h2>
            <p>Define the route, HTTP method, status, headers, and response body.</p>
          </div>

          <form className="form" onSubmit={createEndpoint}>
            <label>
              API name
              <input name="name" value={form.name} onChange={updateField} placeholder="Users API" required />
            </label>

            <div className="two-col">
              <label>
                Method
                <select name="method" value={form.method} onChange={updateField}>
                  <option>GET</option>
                  <option>POST</option>
                  <option>PUT</option>
                  <option>PATCH</option>
                  <option>DELETE</option>
                </select>
              </label>

              <label>
                Status code
                <input name="statusCode" type="number" min="100" max="599" value={form.statusCode} onChange={updateField} required />
              </label>
            </div>

            <label>
              Path
              <input name="path" value={form.path} onChange={updateField} placeholder="/users" required />
            </label>

            <label>
              Content type
              <input name="contentType" value={form.contentType} onChange={updateField} placeholder="application/json" required />
            </label>

            <label>
              Custom headers
              <textarea
                name="headersText"
                value={form.headersText}
                onChange={updateField}
                rows="4"
                placeholder={"X-Trace-Id: demo\nCache-Control: no-store"}
              />
            </label>

            <label>
              Response body
              <textarea
                name="responseBody"
                value={form.responseBody}
                onChange={updateField}
                rows="10"
                placeholder='{\n  "ok": true\n}'
                required
              />
            </label>

            <button type="submit">Create mock endpoint</button>
          </form>
        </section>

        <section className="panel">
          <div className="panel-heading">
            <h2>Saved mock APIs</h2>
            <p>Use these endpoints from your apps, tests, or frontend prototypes.</p>
          </div>

          {error ? <div className="error-banner">{error}</div> : null}
          {loading ? <p className="status-text">Loading endpoints...</p> : null}

          <div className="endpoint-list">
            {endpoints.map((endpoint) => (
              <article className="endpoint-card" key={endpoint.id}>
                <div className="endpoint-top">
                  <span className={`method method-${endpoint.method.toLowerCase()}`}>{endpoint.method}</span>
                  <h3>{endpoint.name}</h3>
                </div>

                <p className="path-line">{endpoint.path}</p>
                <p className="meta-line">
                  Status {endpoint.statusCode} · {endpoint.contentType}
                </p>
                <code className="url-preview">{`/mock${endpoint.path}`}</code>

                <div className="card-actions">
                  <button type="button" className="secondary" onClick={() => testEndpoint(endpoint)}>
                    Test API
                  </button>
                  <button type="button" className="ghost" onClick={() => deleteEndpoint(endpoint.id)}>
                    Delete
                  </button>
                </div>

                {testingId === endpoint.id && testResult ? <pre className="result-box">{testResult}</pre> : null}
              </article>
            ))}

            {!loading && endpoints.length === 0 ? (
              <div className="empty-state">
                <p>No mock APIs yet.</p>
                <span>Create your first endpoint to start testing.</span>
              </div>
            ) : null}
          </div>
        </section>
      </main>
    </div>
  );
}

export default App;

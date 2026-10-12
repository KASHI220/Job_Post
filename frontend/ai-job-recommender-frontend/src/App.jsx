import { useMemo, useRef, useState } from 'react';
import {
  ArrowDownRight,
  ArrowRight,
  Bot,
  BriefcaseBusiness,
  Check,
  ChevronDown,
  CircleHelp,
  FileText,
  GraduationCap,
  LoaderCircle,
  MessageCircle,
  RotateCcw,
  Send,
  Sparkles,
  Target,
  UploadCloud,
  WandSparkles,
  X,
  Zap,
} from 'lucide-react';

const API_BASE_URL = (import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080').replace(/\/$/, '');

function getErrorMessage(response, bodyText) {
  if (response.status === 401 || response.status === 403) {
    return 'The backend blocked this request. Check your Spring Security rules for the API endpoint.';
  }
  if (response.status === 404) {
    return 'The API endpoint was not found. Check that the Spring Boot backend is running on the configured URL.';
  }
  if (bodyText) {
    try {
      const parsed = JSON.parse(bodyText);
      return parsed.message || parsed.error || bodyText;
    } catch {
      return bodyText;
    }
  }
  return `Request failed (${response.status}). Check the Spring Boot terminal for details.`;
}

function formatExperience(value) {
  const years = Number(value);
  if (!Number.isFinite(years) || years < 0) return 'Experience not specified';
  return `${years} ${years === 1 ? 'year' : 'years'} experience`;
}

function initials(profile = 'Role') {
  return profile.split(/\s+/).filter(Boolean).slice(0, 2).map((part) => part[0]?.toUpperCase()).join('') || 'JR';
}

function ScoreRing({ score = 0 }) {
  const safeScore = Math.max(0, Math.min(100, Number(score) || 0));
  return (
    <div className="score-ring" style={{ '--score': `${safeScore}%` }} aria-label={`${safeScore}% skill match`}>
      <div className="score-ring-inner">
        <strong>{safeScore}%</strong>
        <span>match</span>
      </div>
    </div>
  );
}

function SkillPills({ skills, tone = 'neutral', emptyText }) {
  if (!Array.isArray(skills) || skills.length === 0) {
    return emptyText ? <span className="empty-hint">{emptyText}</span> : null;
  }
  return (
    <div className="skill-pills">
      {skills.map((skill, index) => <span className={`skill-pill ${tone}`} key={`${skill}-${index}`}>{skill}</span>)}
    </div>
  );
}

function RecommendationCard({ job, index }) {
  const [expanded, setExpanded] = useState(index === 0);
  const score = Number(job.matchPercentage) || 0;
  const matched = Array.isArray(job.matchedSkills) ? job.matchedSkills : [];
  const missing = Array.isArray(job.missingSkills) ? job.missingSkills : [];
  const required = Array.isArray(job.requiredSkills) ? job.requiredSkills : [];

  return (
    <article className={`job-card ${index === 0 ? 'featured' : ''}`}>
      <div className="job-card-main">
        <div className="company-mark">{initials(job.profile)}</div>
        <div className="job-info">
          <div className="job-title-line">
            <h3>{job.profile || 'Untitled role'}</h3>
            {index === 0 && score > 0 && <span className="best-fit"><Sparkles size={12} /> Best fit</span>}
          </div>
          <p className="job-description">{job.description || 'No job description provided.'}</p>
          <div className="job-meta">
            <span><BriefcaseBusiness size={14} /> {formatExperience(job.experience)}</span>
            {job.jobId && <span className="job-id">ID {job.jobId}</span>}
          </div>
        </div>
        <div className="job-score">
          <ScoreRing score={score} />
        </div>
      </div>

      <div className="match-bar-wrap">
        <div className="match-bar"><span style={{ width: `${Math.max(0, Math.min(100, score))}%` }} /></div>
        <span>{matched.length} of {required.length} skills matched</span>
      </div>

      <button className="details-toggle" type="button" onClick={() => setExpanded((current) => !current)} aria-expanded={expanded}>
        View skill breakdown <ChevronDown size={16} className={expanded ? 'rotated' : ''} />
      </button>

      {expanded && (
        <div className="skill-breakdown">
          <div className="skill-group">
            <div className="skill-group-label"><span className="legend-dot matched-dot" /> Matching skills <b>{matched.length}</b></div>
            <SkillPills skills={matched} tone="matched" emptyText="No direct matches yet" />
          </div>
          <div className="skill-group">
            <div className="skill-group-label"><span className="legend-dot missing-dot" /> Skills to build <b>{missing.length}</b></div>
            <SkillPills skills={missing} tone="missing" emptyText="No missing skills listed" />
          </div>
        </div>
      )}
    </article>
  );
}

function App() {
  const fileInputRef = useRef(null);
  const [file, setFile] = useState(null);
  const [dragActive, setDragActive] = useState(false);
  const [loadingRecommendations, setLoadingRecommendations] = useState(false);
  const [recommendations, setRecommendations] = useState(null);
  const [recommendationError, setRecommendationError] = useState('');
  const [question, setQuestion] = useState('What skills should I focus on to become a Java backend developer?');
  const [answer, setAnswer] = useState('');
  const [asking, setAsking] = useState(false);
  const [askError, setAskError] = useState('');
  const [activeFilter, setActiveFilter] = useState('all');

  const sortedJobs = useMemo(() => {
    if (!Array.isArray(recommendations)) return [];
    return [...recommendations].sort((a, b) => (Number(b.matchPercentage) || 0) - (Number(a.matchPercentage) || 0));
  }, [recommendations]);

  const filteredJobs = useMemo(() => {
    if (activeFilter === 'strong') return sortedJobs.filter((job) => Number(job.matchPercentage) >= 70);
    if (activeFilter === 'good') return sortedJobs.filter((job) => Number(job.matchPercentage) >= 40 && Number(job.matchPercentage) < 70);
    return sortedJobs;
  }, [sortedJobs, activeFilter]);

  const averageMatch = sortedJobs.length
    ? Math.round(sortedJobs.reduce((total, job) => total + (Number(job.matchPercentage) || 0), 0) / sortedJobs.length)
    : 0;

  function useFile(candidate) {
    if (!candidate) return;
    const isPdf = candidate.type === 'application/pdf' || candidate.name?.toLowerCase().endsWith('.pdf');
    if (!isPdf) {
      setRecommendationError('Please choose a PDF resume.');
      return;
    }
    setFile(candidate);
    setRecommendationError('');
  }

  async function handleResumeSubmit(event) {
    event.preventDefault();
    if (!file) {
      setRecommendationError('Choose your resume PDF first.');
      return;
    }

    setLoadingRecommendations(true);
    setRecommendationError('');
    setRecommendations(null);

    try {
      const formData = new FormData();
      formData.append('file', file);
      const response = await fetch(`${API_BASE_URL}/api/recommendations/resume`, {
        method: 'POST',
        body: formData,
      });
      const responseText = await response.text();
      if (!response.ok) throw new Error(getErrorMessage(response, responseText));

      let data;
      try {
        data = JSON.parse(responseText);
      } catch {
        throw new Error('The recommendation API returned a response that was not valid JSON.');
      }
      if (!Array.isArray(data)) {
        throw new Error('Expected a list of job recommendations, but the API returned a different response.');
      }
      setRecommendations(data);
      setActiveFilter('all');
    } catch (error) {
      setRecommendationError(`${error.message || 'Could not fetch recommendations.'} If the browser mentions CORS, allow http://localhost:5173 in your Spring Boot backend.`);
    } finally {
      setLoadingRecommendations(false);
    }
  }

  async function handleAskSubmit(event) {
    event.preventDefault();
    const trimmedQuestion = question.trim();
    if (!trimmedQuestion) {
      setAskError('Enter a question first.');
      return;
    }

    setAsking(true);
    setAskError('');
    setAnswer('');
    try {
      const params = new URLSearchParams({ question: trimmedQuestion });
      const response = await fetch(`${API_BASE_URL}/ai/ask?${params.toString()}`, { method: 'GET' });
      const responseText = await response.text();
      if (!response.ok) throw new Error(getErrorMessage(response, responseText));
      setAnswer(responseText || 'Gemini returned an empty response.');
    } catch (error) {
      setAskError(`${error.message || 'Could not reach Ask AI.'} Check that your backend is running and the endpoint is accessible.`);
    } finally {
      setAsking(false);
    }
  }

  function resetRecommendations() {
    setFile(null);
    setRecommendations(null);
    setRecommendationError('');
    if (fileInputRef.current) fileInputRef.current.value = '';
  }

  return (
    <div className="app-shell">
      <aside className="sidebar">
        <a className="brand" href="#top" aria-label="CareerLens AI home">
          <div className="brand-symbol"><WandSparkles size={20} strokeWidth={2.3} /></div>
          <div className="brand-name">career<span>lens</span><small>AI CAREER MATCH</small></div>
        </a>

        <div className="sidebar-label">WORKSPACE</div>
        <nav className="side-nav" aria-label="Main navigation">
          <a className="nav-item active" href="#recommendations"><Target size={18} /> Job matching <span className="nav-active-dot" /></a>
          <a className="nav-item" href="#ask-ai"><MessageCircle size={18} /> Ask AI</a>
          <a className="nav-item" href="#how-it-works"><CircleHelp size={18} /> How it works</a>
        </nav>

        <div className="sidebar-bottom">
          <div className="sidebar-promo-icon"><Sparkles size={17} /></div>
          <h4>Make your next move count.</h4>
          <p>Understand where your skills fit and what to learn next.</p>
          <a href="#ask-ai">Explore with AI <ArrowRight size={14} /></a>
        </div>
        <div className="sidebar-footer"><span className="status-indicator" /> Spring Boot API <span className="footer-dot">·</span> Gemini</div>
      </aside>

      <main className="main-content" id="top">
        <header className="topbar">
          <div className="breadcrumb">Workspace <span>/</span> <strong>Job matching</strong></div>
          <div className="topbar-right"><span className="secure-label"><Zap size={14} /> AI-powered career tools</span><div className="avatar">CL</div></div>
        </header>

        <section className="hero-section">
          <div className="hero-copy">
            <div className="eyebrow"><span className="eyebrow-sparkle"><Sparkles size={13} /></span> YOUR NEXT CHAPTER STARTS HERE</div>
            <h1>Find the roles that<br /><span>fit your skills.</span></h1>
            <p>Upload your resume. We'll compare your skills with job requirements and show where you stand — with a little help from AI.</p>
            <div className="hero-footnote"><span><Check size={14} /> Resume-based matching</span><span><Check size={14} /> Skill gap insights</span></div>
          </div>
          <div className="hero-visual" aria-hidden="true">
            <div className="orbit orbit-one" /><div className="orbit orbit-two" />
            <div className="hero-orb"><div className="hero-orb-center"><Target size={39} strokeWidth={1.5} /></div><div className="hero-orb-dot dot-one" /><div className="hero-orb-dot dot-two" /><div className="hero-orb-dot dot-three" /></div>
            <div className="floating-card floating-card-top"><div className="floating-card-icon violet"><Sparkles size={15} /></div><div><strong>Smart matching</strong><small>Skills meet opportunity</small></div></div>
            <div className="floating-card floating-card-bottom"><div className="mini-progress"><span /></div><div><strong>Good things ahead</strong><small>Your next role is out there</small></div><ArrowDownRight size={17} /></div>
          </div>
        </section>

        <section className="workspace-grid" id="recommendations">
          <div className="panel upload-panel">
            <div className="panel-heading">
              <div className="section-icon upload-icon"><FileText size={18} /></div>
              <div><div className="panel-kicker">STEP 01</div><h2>Match my resume</h2></div>
              <span className="optional-tag">PDF</span>
            </div>
            <p className="panel-intro">We'll read your resume, identify your skills, and compare them with the jobs in your database.</p>

            <form onSubmit={handleResumeSubmit}>
              <div
                className={`dropzone ${dragActive ? 'drag-active' : ''} ${file ? 'has-file' : ''}`}
                onDragOver={(event) => { event.preventDefault(); setDragActive(true); }}
                onDragLeave={() => setDragActive(false)}
                onDrop={(event) => { event.preventDefault(); setDragActive(false); useFile(event.dataTransfer.files?.[0]); }}
                onClick={() => fileInputRef.current?.click()}
                role="button"
                tabIndex={0}
                onKeyDown={(event) => { if (event.key === 'Enter' || event.key === ' ') { event.preventDefault(); fileInputRef.current?.click(); } }}
                aria-label="Choose or drop a resume PDF"
              >
                <input ref={fileInputRef} type="file" accept=".pdf,application/pdf" hidden onChange={(event) => useFile(event.target.files?.[0])} />
                {file ? (
                  <>
                    <div className="file-icon"><FileText size={23} /></div>
                    <strong className="selected-file-name">{file.name}</strong>
                    <span>{(file.size / (1024 * 1024)).toFixed(2)} MB · Ready to analyze</span>
                    <button className="remove-file" type="button" onClick={(event) => { event.stopPropagation(); resetRecommendations(); }} aria-label="Remove selected file"><X size={14} /></button>
                  </>
                ) : (
                  <>
                    <div className="upload-cloud"><UploadCloud size={25} /></div>
                    <strong>Drop your resume here</strong>
                    <span>or <b>browse files</b> from your computer</span>
                    <small>PDF only · Choose a text-based PDF for best results</small>
                  </>
                )}
              </div>
              <div className="upload-security"><span className="tiny-lock">◈</span> Your resume is sent to your configured backend for analysis.</div>
              <button className="primary-button full-width" type="submit" disabled={loadingRecommendations || !file}>
                {loadingRecommendations ? <><LoaderCircle size={17} className="spin" /> Analyzing resume...</> : <><Sparkles size={17} /> Find my job matches <ArrowRight size={16} /></>}
              </button>
            </form>
            {loadingRecommendations && <div className="loading-note"><span className="loading-dot" /> Gemini is reading your resume and comparing job skills. This can take a little while.</div>}
            {recommendationError && <div className="error-message" role="alert"><CircleHelp size={17} /><span>{recommendationError}</span></div>}
          </div>

          <div className="panel ask-panel" id="ask-ai">
            <div className="panel-heading">
              <div className="section-icon ask-icon"><Bot size={19} /></div>
              <div><div className="panel-kicker">YOUR AI SIDEKICK</div><h2>Ask AI anything</h2></div>
              <span className="online-tag"><span /> Gemini</span>
            </div>
            <p className="panel-intro">Ask about Java, interviews, career paths, or what you should learn next.</p>
            <div className="suggested-label">TRY ASKING</div>
            <div className="prompt-suggestions">
              {[
                'How can I improve my Java backend resume?',
                'Explain REST APIs in simple words.',
                'What should I prepare for a Spring Boot interview?',
              ].map((suggestion) => (
                <button key={suggestion} className="suggestion-chip" type="button" onClick={() => setQuestion(suggestion)}>{suggestion}<ArrowRight size={13} /></button>
              ))}
            </div>
            <form className="ask-form" onSubmit={handleAskSubmit}>
              <label className="sr-only" htmlFor="ai-question">Your question</label>
              <textarea id="ai-question" value={question} onChange={(event) => setQuestion(event.target.value)} rows={3} placeholder="Ask your career question..." />
              <div className="ask-form-bottom"><span><Sparkles size={13} /> Powered by Gemini</span><button className="send-button" type="submit" disabled={asking || !question.trim()} aria-label="Send question">{asking ? <LoaderCircle className="spin" size={17} /> : <Send size={17} />}</button></div>
            </form>
            {asking && <div className="ask-loading"><LoaderCircle className="spin" size={15} /> Thinking through your question...</div>}
            {askError && <div className="error-message compact" role="alert"><CircleHelp size={16} /><span>{askError}</span></div>}
            {answer && <div className="ai-answer"><div className="answer-label"><div className="answer-bot"><Bot size={15} /></div><strong>Gemini's answer</strong><button type="button" onClick={() => setAnswer('')} aria-label="Clear answer"><X size={15} /></button></div><p>{answer}</p></div>}
          </div>
        </section>

        {Array.isArray(recommendations) && (
          <section className="results-section" aria-live="polite">
            <div className="results-heading">
              <div><div className="panel-kicker">YOUR RESULTS</div><h2>Recommended for you <span className="result-count">{sortedJobs.length}</span></h2><p>Ranked by how closely your extracted skills match each job's listed skills.</p></div>
              <button type="button" className="text-button" onClick={resetRecommendations}><RotateCcw size={15} /> Analyze another resume</button>
            </div>

            {sortedJobs.length > 0 && (
              <div className="stats-row">
                <div className="stat-card"><div className="stat-icon lavender"><BriefcaseBusiness size={17} /></div><div><span>Jobs evaluated</span><strong>{sortedJobs.length}</strong></div></div>
                <div className="stat-card"><div className="stat-icon mint"><Target size={17} /></div><div><span>Top skill match</span><strong>{Math.round(Number(sortedJobs[0]?.matchPercentage) || 0)}<small>%</small></strong></div></div>
                <div className="stat-card"><div className="stat-icon peach"><GraduationCap size={17} /></div><div><span>Average match</span><strong>{averageMatch}<small>%</small></strong></div></div>
              </div>
            )}

            {sortedJobs.length > 0 && (
              <div className="results-toolbar"><span className="results-total">Showing <strong>{filteredJobs.length}</strong> of {sortedJobs.length} roles</span><div className="filter-tabs" aria-label="Filter recommendations"><button className={activeFilter === 'all' ? 'selected' : ''} onClick={() => setActiveFilter('all')} type="button">All matches</button><button className={activeFilter === 'strong' ? 'selected' : ''} onClick={() => setActiveFilter('strong')} type="button">Strong fit · 70%+</button><button className={activeFilter === 'good' ? 'selected' : ''} onClick={() => setActiveFilter('good')} type="button">Potential · 40–69%</button></div></div>
            )}

            {sortedJobs.length === 0 ? (
              <div className="empty-results"><div className="empty-results-icon"><BriefcaseBusiness size={24} /></div><h3>No job matches returned</h3><p>The endpoint responded successfully, but no job recommendations were found. Check the MongoDB collection and job data.</p></div>
            ) : filteredJobs.length === 0 ? (
              <div className="empty-results small-empty"><h3>No jobs in this range</h3><p>Try viewing all matches to see every returned role.</p><button className="text-button" type="button" onClick={() => setActiveFilter('all')}>Show all jobs <ArrowRight size={14} /></button></div>
            ) : (
              <div className="job-list">{filteredJobs.map((job, index) => <RecommendationCard key={job.jobId || `${job.profile}-${index}`} job={job} index={index} />)}</div>
            )}
            <div className="results-disclaimer"><Sparkles size={15} /><span><strong>How to read these scores:</strong> Match percentages are based on the listed skills only. They are a guide, not a guarantee of job suitability; experience is displayed separately.</span></div>
          </section>
        )}

        <section className="how-section" id="how-it-works">
          <div className="how-header"><div className="panel-kicker">SIMPLE BY DESIGN</div><h2>From resume to next step.</h2><p>Three steps to turn your experience into a clearer job search.</p></div>
          <div className="how-steps">
            <div className="how-step"><div className="step-icon step-lavender"><UploadCloud size={20} /></div><span className="step-number">01</span><h3>Upload your resume</h3><p>PDFBox extracts the text from your PDF so it can be analyzed.</p></div>
            <div className="step-connector"><ArrowRight size={18} /></div>
            <div className="how-step"><div className="step-icon step-peach"><WandSparkles size={20} /></div><span className="step-number">02</span><h3>AI reads your skills</h3><p>Gemini structures your skills, experience, and education.</p></div>
            <div className="step-connector"><ArrowRight size={18} /></div>
            <div className="how-step"><div className="step-icon step-mint"><Target size={20} /></div><span className="step-number">03</span><h3>Explore your matches</h3><p>Java compares your skills with MongoDB jobs and ranks the results.</p></div>
          </div>
        </section>

        <footer className="page-footer"><a className="footer-brand" href="#top">careerlens<span> AI</span></a><span>Built with React · Spring Boot · Gemini</span><a href="#top">Back to top ↑</a></footer>
      </main>
    </div>
  );
}

export default App;

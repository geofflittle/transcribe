\documentclass[12pt]{extarticle}
\usepackage[margin=1in, headheight=30pt, headsep=5pt, footnotesep=5pt]{geometry}
\usepackage{fancyhdr}
\usepackage{lineno}
\usepackage{setspace}
\usepackage{times}

\pagestyle{fancy}
\fancyhf{}
\fancyhead[L]{${documentTitle}}
\fancyhead[C]{}
\fancyhead[R]{Case: ${caseName}}
\fancyfoot[L]{${info}}
\fancyfoot[C]{}
\fancyfoot[R]{Page: \thepage\\${filename}}

\renewcommand{\headrulewidth}{1pt}
\renewcommand{\footrulewidth}{1pt}

\begin{document}
	\emergencystretch 3em
	\linenumbers
	\doublespacing
	\large
	\ttfamily
    ${body}
\end{document}

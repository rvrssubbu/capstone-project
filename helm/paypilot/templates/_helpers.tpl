{{- define "paypilot.namespace" -}}
{{- .Values.namespace.name -}}
{{- end -}}

{{- define "paypilot.labels" -}}
app.kubernetes.io/name: paypilot
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
helm.sh/chart: {{ .Chart.Name }}-{{ .Chart.Version | replace "+" "_" }}
{{- end -}}

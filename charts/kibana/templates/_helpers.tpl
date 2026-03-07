{{- define "kibana.fullname" -}}
{{- printf "%s-kibana" .Release.Name | trunc 63 | trimSuffix "-" -}}
{{- end -}}
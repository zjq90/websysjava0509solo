module.exports = function(source) {
  if (source.includes('recyclableRender')) {
    const hasDefinition = /(?:var|const|let|function)\s+recyclableRender/.test(source)
    if (!hasDefinition) {
      source = 'var recyclableRender = function(){};\n' + source
    }
  }
  return source
}

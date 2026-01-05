#!/bin/bash
# Script para limpiar el repo hibernate-order-management
# Ejecutar desde la raíz del repositorio clonado

set -e  # Salir si hay error

echo "🧹 Limpiando repositorio hibernate-order-management..."
echo ""

# Verificar que estamos en un repo git
if [ ! -d ".git" ]; then
    echo "❌ Error: No estás en la raíz de un repositorio git"
    echo "   Ejecuta: cd /ruta/a/hibernate-order-management"
    exit 1
fi

# Verificar que es el repo correcto
if [ ! -f "pom.xml" ]; then
    echo "❌ Error: No parece ser el repo correcto (no hay pom.xml)"
    exit 1
fi

echo "📁 Paso 1: Actualizando .gitignore..."

# Crear/actualizar .gitignore con las entradas necesarias
cat >> .gitignore << 'EOF'

# === Añadido por script de limpieza ===
# Build output
target/
build/
out/

# Logs
logs/
*.log

# IDE
.idea/
*.iml
.project
.classpath
.settings/

# OS
.DS_Store
Thumbs.db

# Database files (si no quieres compartir datos de ejemplo)
# *.db
EOF

echo "✅ .gitignore actualizado"

echo ""
echo "📁 Paso 2: Eliminando carpetas del tracking de git..."

# Eliminar target/ del tracking (si existe)
if [ -d "target" ]; then
    git rm -r --cached target/ 2>/dev/null || true
    echo "✅ target/ eliminado del tracking"
else
    echo "ℹ️  target/ no existe localmente"
fi

# Eliminar logs/ del tracking (si existe)
if [ -d "logs" ]; then
    git rm -r --cached logs/ 2>/dev/null || true
    echo "✅ logs/ eliminado del tracking"
else
    echo "ℹ️  logs/ no existe localmente"
fi

echo ""
echo "📝 Paso 3: Creando commit..."

git add .gitignore
git add -A

# Verificar si hay cambios para commitear
if git diff --cached --quiet; then
    echo "ℹ️  No hay cambios para commitear"
else
    git commit -m "chore: clean up repo structure

- Remove target/ and logs/ from version control
- Update .gitignore with standard Java/Maven exclusions
- Keep build artifacts local only"
    echo "✅ Commit creado"
fi

echo ""
echo "🚀 Paso 4: Push a GitHub..."
read -p "¿Quieres hacer push ahora? (s/n): " respuesta

if [[ "$respuesta" =~ ^[Ss]$ ]]; then
    git push origin main
    echo "✅ Push completado"
else
    echo "ℹ️  Recuerda hacer: git push origin main"
fi

echo ""
echo "=========================================="
echo "✅ ¡Limpieza completada!"
echo "=========================================="
echo ""
echo "📋 TAREAS MANUALES en GitHub (web):"
echo ""
echo "1. Ve a: https://github.com/ejnero-dev/hibernate-order-management"
echo ""
echo "2. Click en ⚙️ (Settings) junto a 'About'"
echo ""
echo "3. Añade DESCRIPTION:"
echo "   Order management system demonstrating Hibernate ORM, design patterns (DAO, Abstract Factory, Strategy), and dual interfaces (Console/Swing)"
echo ""
echo "4. Añade TOPICS (separados por Enter):"
echo "   java"
echo "   hibernate"
echo "   orm"
echo "   jpa"
echo "   swing"
echo "   design-patterns"
echo "   maven"
echo "   sqlite"
echo "   portfolio-project"
echo ""
echo "5. Marca 'Releases' y 'Packages' si no los usas para ocultarlos"
echo ""

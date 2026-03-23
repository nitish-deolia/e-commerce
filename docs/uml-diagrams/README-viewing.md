# Online UML Diagram Tools

## 1. PlantUML Online Editor
- **URL**: https://www.plantuml.com/plantuml/uml/
- **How to use**:
  1. Copy the PlantUML code from our `.puml` files
  2. Paste into the online editor
  3. Click "Submit" to generate visual diagrams

## 2. Draw.io (diagrams.net)
- **URL**: https://app.diagrams.net/
- **Features**: Free, supports UML, export to PNG/SVG

## 3. Lucidchart
- **URL**: https://www.lucidchart.com/
- **Features**: Professional UML diagramming tool

## 4. Mermaid Live Editor
- **URL**: https://mermaid.live/
- **Features**: Great for sequence diagrams and flowcharts

## 5. VS Code Extensions
- **PlantUML Extension**: Search for "PlantUML" in VS Code extensions
- **Mermaid Preview**: Search for "Mermaid Preview" in VS Code extensions

## Example: Convert Our Auth Service Diagram

Copy this PlantUML code to any online editor:

```
@startuml Auth Service Class Diagram
class User {
    - userId: UUID
    - username: String
    - email: String
    - passwordHash: String
    - role: Role
    + register()
    + login()
}

class Role {
    - roleId: UUID
    - name: String
    - permissions: List
    + canCreate()
    + canRead()
}

User --> Role : has
@enduml
```

## Quick Start Commands (once PlantUML is installed)

```bash
# Generate PNG from PlantUML file
plantuml auth-service.puml

# Generate SVG
plantuml -tsvg auth-service.puml

# Generate all diagrams in folder
plantuml *.puml
```
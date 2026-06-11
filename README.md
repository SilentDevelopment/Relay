# Relay

Relay is a command framework for Java, with a Paper platform module for Minecraft servers.

It is designed around three ideas:

1. **A clean, platform-agnostic command model**
2. **A reusable parsing / matching / suggestion engine**
3. **Thin platform adapters** that expose the model naturally on specific runtimes such as Paper

Relay is not trying to be “just a command executor helper”. It is a structured command system with:

- typed arguments
- overloads / signatures
- subcommands
- flags and value options
- requirements / access checks
- help generation
- suggestion providers
- platform-specific authoring conveniences

---

## Modules

### `relay-api`
The public API and shared abstractions.

This module contains the core contracts and model types such as:

- `Command`
- `CommandDefinition`
- `Argument`
- `ArgumentType`
- `CommandDispatcher`
- `CommandMatcher`
- `CommandRequirement`
- `CommandHelpProvider`
- `CommandResponseRenderer`

If you are building on Relay as a library, this is the conceptual surface area.

### `relay-core`
The default implementation of the Relay API.

This module contains:

- default argument readers
- default parsers
- the default matcher
- the default dispatcher
- the default suggester
- default help provider / usage formatter
- the generic `CommandBuilder`
- default command and option implementations

This is the engine that makes the API useful.

### `relay-paper`
The Paper platform adapter.

This module adds:

- Paper command manager
- lifecycle-based registration
- bootstrap support
- Paper-specific builder conveniences
- Paper-specific argument types
- Adventure / Paper rendering support
- Paper command contexts and handlers

This is the module consumer Paper plugins will use.

---

## Design

Relay is intentionally split into layers.

### 1. Command model
The command tree is represented as immutable command definitions and runtime command models.

A command can have:

- a name
- aliases
- subcommands
- signatures
- options
- requirements
- suggestion providers

### 2. Parsing and matching
Relay parses input using its own argument readers and parsers.

The matcher is responsible for:

- descending through subcommands
- parsing options
- choosing the best matching signature
- producing a `CommandMatchResult`

### 3. Dispatch
The dispatcher:

- finds the correct root command
- parses the remainder
- checks requirements
- finds the bound handler
- executes the command
- returns a structured `CommandDispatchResult`

### 4. Help and suggestions
Relay can generate structured help entries and usage lists from the command tree. Suggestions are produced using the command model, signatures, and registered suggestion providers.

### 5. Platform adapters
A platform module takes the generic engine and exposes it in a platform-native way.

For Paper, that means:

- Paper lifecycle registration
- Paper command bootstrap support
- Paper-specific argument types such as players, worlds, materials, etc.
- Adventure component-based rendering

---
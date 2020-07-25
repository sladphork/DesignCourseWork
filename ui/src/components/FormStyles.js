import styled from 'styled-components';

const Card = styled.div`
  box-sizing: border-box;
  max-width: 410px;
  margin: 0 auto;
  padding: 0 2rem;
  display: flex;
  flex-direction: column;
  align-items: center;
`;

const Form = styled.div`
  display: flex;
  flex-direction: column;
  width: 100%;
`;

const Input = styled.input`
  padding: 1rem;
  border: 1px solid #34000d;
  margin-bottom: 1rem;
  font-size: 0.8rem;
`;

const Button = styled.button`
  background: linear-gradient(to bottom, #ffd700, #b39700);
  border-color: #34000d;
  border-radius: 3px;
  padding: 1rem;
  color: white;
  font-weight: 700;
  width: 100%;
  margin-bottom: 1rem;
  font-size: 0.8rem;
`;

const Logo = styled.img`
  width: 50%;
  margin-bottom: 1rem;
`;

const Error = styled.div`
  background-color: red;
`;

const Title = styled.h1`
  text-align: center;
  font-style: italic;
  color: #ffd700
`

export { Form, Input, Button, Logo, Card, Error, Title };